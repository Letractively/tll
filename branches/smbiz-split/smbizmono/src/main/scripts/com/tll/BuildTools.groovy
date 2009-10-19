/**
 * The Logic Lab
 * @author jpk
 * Mar 28, 2009
 */
 
package com.tll;

import com.google.inject.Guice;

import com.tll.config.Config;
import com.tll.config.ConfigRef;
import com.tll.ConfigProcessor;
import com.tll.dao.IDbShell;
import com.tll.di.SmbizDb4oDaoModule;
import com.tll.di.test.Db4oDbShellModule;
import com.tll.di.SmbizModelModule;
import com.tll.model.test.IEntityGraphPopulator;
import com.tll.di.test.EGraphModule;

// gmaven does't pick up test classes in the maven classpath 
// so we need to re-define the smbiz test deps here tragically

//import com.tll.di.SmbizEGraphModule;
//import com.tll.model.SmbizEntityGraphBuilder;
import com.tll.model.*;
import com.google.inject.Inject;
import com.tll.model.test.AbstractEntityGraphPopulator;
import com.tll.model.test.EntityBeanFactory;
import com.tll.util.EnumUtil;

/**
 * Redux hack (gmaven doesn't pick up test-classes dir!)
 */
public final class SmbizEntityGraphBuilder extends AbstractEntityGraphPopulator {

	private static final int numIsps = 3;
	private static final int numMerchants = numIsps * numIsps;
	private static final int numCustomers = 2 + numIsps * 2 + numMerchants * 2;

	/**
	 * Constructor
	 * @param ebf the required entity bean factory
	 */
	@Inject
	public SmbizEntityGraphBuilder(EntityBeanFactory ebf) {
		super(ebf);
	}

	@Override
	protected void stub() {
		try {
			stubRudimentaryEntities();
			stubAccounts();
			stubInterfaces();
			stubUsers();
		}
		catch(final Exception e) {
			throw new IllegalStateException("Unable to stub entity graph: " + e.getMessage());
		}
	}

	private void stubRudimentaryEntities() throws Exception {
		add(Currency.class, false);

		// app properties
		AppProperty ap;
		ap = add(AppProperty.class, false);
		ap.setName("locale");
		ap.setValue("US");
		ap = add(AppProperty.class, false);
		ap.setName("default.iso4217");
		ap.setValue("usd");
		ap = add(AppProperty.class, false);
		ap.setName("default.country");
		ap.setValue("usa");

		// addresses
		addN(Address.class, true, 10);

		// payment infos
		add(PaymentInfo.class, false);

		// user authorities
		addAll(Authority.class);
	}

	private <A extends Account> A stubAccount(Class<A> type, int num) throws Exception {
		final A a = add(type, false);

		if(num > 0) {
			a.setName(a.getName() + " " + Integer.toString(num));
		}

		a.setCurrency(getRandomEntity(Currency.class));

		a.setPaymentInfo(getRandomEntity(PaymentInfo.class));

		// account addresses upto 5
		final int numAddresses = randomInt(2); // make 0 or 1 for quicker debugging
		if(numAddresses > 0) {
			int ai = 0;
			final Set<AccountAddress> set = addN(AccountAddress.class, true, numAddresses);
			for(final AccountAddress aa : set) {
				aa.setAddress(getNthEntity(Address.class, ++ai));
				aa.setType(EnumUtil.fromOrdinal(AddressType.class, randomInt(AddressType.values().length)));
				a.addAccountAddress(aa);
			}
		}

		// create a random number of histories for this account upto 5
		final int numHistories = randomInt(6);
		if(numHistories > 0) {
			for(int i = 0; i < numHistories; i++) {
				final AccountHistory ah = add(AccountHistory.class, true);
				ah.setAccount(a);
				makeUnique(ah);
			}
		}

		// create a product set for this account upto 5
		final int numProducts = randomInt(6);
		if(numProducts > 0) {
			for(int i = 0; i < numProducts; i++) {
				final ProductInventory pi = add(ProductInventory.class, true);
				final ProductGeneral pg = generateEntity(ProductGeneral.class, true);
				pi.setProductGeneral(pg);
				pi.setAccount(a);
				makeUnique(pi);
			}
		}

		// create product categories for these account products upto 5
		final int numCategories = randomInt(6);
		if(numCategories > 0) {
			for(int i = 0; i < numCategories; i++) {
				final ProductCategory pc = add(ProductCategory.class, true);
				pc.setAccount(a);
				makeUnique(pc);
			}
		}

		// TODO bind products to categories

		// create some sales taxes upto 5
		final int numSalesTaxes = randomInt(6);
		if(numSalesTaxes > 0) {
			for(int i = 0; i < numSalesTaxes; i++) {
				final SalesTax st = add(SalesTax.class, true);
				st.setAccount(a);
				makeUnique(st);
			}
		}

		return a;
	}

	private void stubAccounts() throws Exception {
		// asp
		final Asp asp = stubAccount(Asp.class, 0);
		asp.setName(Asp.ASP_NAME);

		// isps
		final Isp[] isps = new Isp[numIsps];
		for(int i = 0; i < numIsps; i++) {
			final Isp isp = stubAccount(Isp.class, i + 1);
			isp.setParent(asp);
			isps[i] = isp;
		}

		// merchants
		final Merchant[] merchants = new Merchant[numMerchants];
		for(int i = 0; i < numMerchants; i++) {
			final Merchant m = stubAccount(Merchant.class, i + 1);
			final int ispIndex = i / numIsps;
			m.setParent(isps[ispIndex]);
			merchants[i] = m;
		}

		// customers
		final Customer[] customers = new Customer[numCustomers];
		for(int i = 0; i < numCustomers; i++) {
			final Customer c = stubAccount(Customer.class, i + 1);

			// create customer account binder entity
			final CustomerAccount ca = add(CustomerAccount.class, false);
			Account parent;
			ca.setCustomer(c);
			if(i < 2) {
				ca.setAccount(asp);
				parent = asp;
			}
			else if(i < (2 + 2 * numIsps)) {
				final int ispIndex = i / (2 * numIsps); // TODO verify the math
				final Isp isp = isps[ispIndex];
				parent = isp;
				ca.setAccount(isp);
				customers[i] = c;
			}
			else {
				final int merchantIndex = i / (2 * numMerchants); // TODO verify the math
				final Merchant merchant = merchants[merchantIndex];
				parent = merchant;
				ca.setAccount(merchant);
			}

			// create initial visitor record
			final Visitor v = add(Visitor.class, true);
			v.setAccount(parent);
			ca.setInitialVisitorRecord(v);
		}
	}

	private static InterfaceOption findInterfaceOption(String ioCode, Set<InterfaceOption> options) {
		for(final InterfaceOption io : options) {
			if(ioCode.equals(io.getCode())) {
				return io;
			}
		}
		return null;
	}

	private static InterfaceOptionParameterDefinition findParameterDefinition(String pdCode,
			Set<InterfaceOptionParameterDefinition> params) {
		for(final InterfaceOptionParameterDefinition pd : params) {
			if(pdCode.equals(pd.getCode())) {
				return pd;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void stubInterfaces() throws Exception {
		final Set<Interface> intfs = (Set<Interface>) getNonNullEntitySet(Interface.class);
		intfs.addAll(addAll(InterfaceSingle.class));
		intfs.addAll(addAll(InterfaceSwitch.class));
		intfs.addAll(addAll(InterfaceMulti.class));

		final Set<InterfaceOption> ios = addAll(InterfaceOption.class);

		final Set<InterfaceOptionParameterDefinition> pds = addAll(InterfaceOptionParameterDefinition.class);

		for(final Interface intf : intfs) {
			if(Interface.CODE_CROSS_SELL.equals(intf.getCode())) {
				final InterfaceOption io = findInterfaceOption("crosssell-switch", ios);
				if(io != null) {
					for(int i = 1; i <= 3; i++) {
						final InterfaceOptionParameterDefinition pd = findParameterDefinition("crosssellP" + i, pds);
						if(pd != null) {
							io.addParameter(pd);
						}
					}
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_PAYMENT_METHOD.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("visa", ios);
				if(io != null) {
					intf.addOption(io);
				}
				io = findInterfaceOption("mc", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_PAYMENT_PROCESSOR.equals(intf.getCode())) {
				InterfaceOption io = findInterfaceOption("native_payproc", ios);
				if(io != null) {
					intf.addOption(io);
				}
				io = findInterfaceOption("verisign_payproc", ios);
				if(io != null) {
					InterfaceOptionParameterDefinition pd = findParameterDefinition("verisignP1", pds);
					if(pd != null) {
						io.addParameter(pd);
					}
					pd = findParameterDefinition("verisignP2", pds);
					if(pd != null) {
						io.addParameter(pd);
					}
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_SALES_TAX.equals(intf.getCode())) {
				final InterfaceOption io = findInterfaceOption("native_salestax", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
			else if(Interface.CODE_SHIP_METHOD.equals(intf.getCode())) {
				final InterfaceOption io = findInterfaceOption("native_shipmethod", ios);
				if(io != null) {
					intf.addOption(io);
				}
			}
		}
	}

	private void stubUsers() throws Exception {
		final User u = add(User.class, false);
		u.addAuthority(getRandomEntity(Authority.class));
		u.setAccount(getNthEntity(Asp.class, 1));
		u.setAddress(getRandomEntity(Address.class));
	}

}

 /**
  * Redux hack (gmaven doesn't pick up test-classes dir!)
  */
public class SmbizEGraphModule extends EGraphModule {

 	@Override
 	protected URI getBeanDefRef() {
 		try {
 			return new URI("target/classes/mock-entities.xml");
 		}
 		catch(final URISyntaxException e) {
 			throw new IllegalStateException("Can't find mock entities file", e);
 		}
 	}

 	@Override
 	protected Class<? extends IEntityGraphPopulator> getEntityGraphBuilderImplType() {
 		return SmbizEntityGraphBuilder.class;
 	}
}

/**
 * BuildTools - Utility class for smbiz project building.
 * @author jpk
 */
public final class BuildTools {
	 
	/**
	 * Process resources necessary for war assembly. 
	 */
	static void processWarResources(def project, def ant) {
		BuildTools b = new BuildTools(project, ant);
	    b.saveConfig();
	    b.generateGwtConstantsFile();
	    b.generateWebXml();
	    b.copyWebappResources();
      	b.stubDbIfNecessary()
	}
	
	static final String DEFAULT_STAGE = 'debug'
	static final String DEFAULT_DAO_IMPL = 'db40'
	static final String DEFAULT_SECURITY_IMPL = 'none'
	 
	static final int FLAG_ALL = 0;
	static final int FLAG_DB_DB4O = 1;
	static final int FLAG_DB_JDO = 1 << 1;
	static final int FLAG_SECURITY_ACEGI = 1 << 2;
	static final int FLAG_SECURITY_NONE = 1 << 3;
	 
	static final def NL = System.getProperty("line.separator")

	static final def regex_db_db4o = /(?s)<!-- START DB DB4O -->(.*)<!-- END DB DB4O -->/
	static final def regex_db_jdo = /(?s)<!-- START DB JDO -->(.*)<!-- END DB JDO -->/
	static final def regex_security_acegi = /(?s)<!-- START SECURITY ACEGI -->(.*)<!-- END SECURITY ACEGI -->/
	static final def regex_security_none = /(?s)<!-- START NO SECURITY -->(.*)<!-- END NO SECURITY -->/
	
	// all di module ref arrays of format: [name, flags]
	// where flags indicates the eligibility of inclusion into the 
	// target web.xml based on the loaded config state 
	static final def DI_MODULES_ALL = [
	  ['com.tll.di.VelocityModule', FLAG_ALL],
	  ['com.tll.di.MailModule', FLAG_ALL],
	  ['com.tll.di.RefDataModule', FLAG_ALL],
	  ['com.tll.di.EmailExceptionHandlerModule', FLAG_DB_JDO],
	  ['com.tll.di.LogExceptionHandlerModule', FLAG_DB_DB4O],
	  ['com.tll.di.SmbizModelModule', FLAG_ALL],
	  ['com.tll.di.JdoDaoModule', FLAG_DB_JDO],
	  ['com.tll.di.SmbizDb4oDaoModule', FLAG_DB_DB4O],
	  ['com.tll.di.SmbizEntityServiceFactoryModule', FLAG_ALL],
	  ['com.tll.di.SmbizMarshalModule', FLAG_ALL],
	  ['com.tll.di.SmbizClientPersistModule', FLAG_ALL],
	  ['com.tll.di.SmbizListingModule', FLAG_ALL],
	  ['com.tll.di.SmbizAcegiModule', FLAG_SECURITY_ACEGI],
	  ['com.tll.di.AppModule', FLAG_ALL],
	]
	
	// all di handler (bootstrapper) refs
	static final def DI_HANDLERS_ALL = [
	  ['com.tll.server.Db4oBootstrapper', FLAG_DB_DB4O],
	  ['com.tll.server.rpc.entity.PersistContextBootstrapper', FLAG_ALL],
	  ['com.tll.server.rpc.listing.ListingContextBootstrapper', FLAG_ALL],
	  ['com.tll.server.SecurityContextBootstrapper', FLAG_SECURITY_ACEGI],
	  ['com.tll.server.AppContextBootstrapper', FLAG_ALL],
	]

	/**
	 * Replaces all occurrences of ${prop.name} with the property value held in the provided property map
	 * NOTE: No variable interpolation is performed.
	 * @param str The string that is searched for property place-holders
	 * @param props The property map
	 * @return String containing resolved property values
	 */
	def rplProps = {String str, Map props ->
        String rval = str;
        props.each { key, val -> rval = rval.replace('${' + key + '}', val) }
        return rval;
	}
	
	/**
	 * maven project, and ant refs (via gmaven plugin)
	 */
	private def project, ant;
	
	/**
	 * The project base directory.
	 */
	private String basedir;
		
	/**
	 * The generated Config instance employed for the build.
	 */
	private Config config;
	
	/**
	 * The project properties.
	 */
	private String stage, daoImpl, securityImpl;
	
	private int daoImplFlag, securityImplFlag;
	
	private int flags;
	
	/**
	 * Constructor
	 * @param project the maven project ref
	 * @param ant the maven ant ref
	 */
	public BuildTools(def project, def ant) {
		if(project == null || ant == null) {
			throw new IllegalArgumentException('Null project and/or ant ref(s).')
		}
		this.project = project;
		this.ant = ant;
		init();
	}
	
	/**
	 * Init routing called by constructor.
	 */
	private void init() {
		this.basedir = project.basedir.toString()
		// load the config
		this.config = ConfigProcessor.merge(basedir + "/src/main/resources", project.properties.mode, 'local')
		
		// obtain the stage
		this.stage = project.properties.stage
	
		// obtain the dao impl
		this.daoImpl = project.properties.daoImpl
		if(daoImpl == null) {
			daoImpl = DEFAULT_DAO_IMPL
			println 'No dao impl specified in project properties reverted to default' 
		}
		println "daoImpl: ${daoImpl}"
		switch(daoImpl) {
			case 'db4o': daoImplFlag = FLAG_DB_DB4O; break
			case 'jdo': daoImplFlag = FLAG_DB_JDO; break
			default: throw new IllegalArgumentException("Unhandled dao impl: ${daoImpl}")
		}
		
		// obtain the security impl
		this.securityImpl = project.properties.securityImpl
		if(securityImpl == null) {
			securityImpl = DEFAULT_SECURITY_IMPL
			println 'No security impl specified in project properties reverted to default' 
		}
		println "securityImpl: ${securityImpl}"
		switch(securityImpl) {
			case 'acegi': securityImplFlag = FLAG_SECURITY_ACEGI; break
			case 'none': securityImplFlag = FLAG_SECURITY_NONE; break
			default: throw new IllegalArgumentException("Unhandled security impl: ${securityImpl}")
		}

		// set 'debug' property to build config
		config.setProperty('stage', this.stage)
		
		// set the flags
		this.flags = 0 | daoImplFlag | securityImplFlag;
	}
	
	/**
	 * Copies webapp resources to the target war dir.
	 */
	public void copyWebappResources() {
		println 'Copying webapp resources..'
		String sdir = basedir + '/src/main/webapp'
		String tdir = basedir + '/target/war'
		ant.mkdir(dir: tdir)
		ant.copy(todir: tdir) {
			fileset(dir: sdir) {
				exclude(name: '**/web.xml')
			}
		}
		println 'webapp resources copied.'
	}
	
	/**
	 * Generates the web.xml file based on the configuration/project state.
	 */
	public void generateWebXml() {
		println 'Generating web.xml..'
		
		Map props = [:];
		
		// add the di props
		def sb = new StringBuilder(1024)
		DI_MODULES_ALL.each { elm ->
			int flag = elm[1]
			if(flag == FLAG_ALL || ((flag & flags) == flag)) {
				println "-Module: ${elm[0]}"
				sb.append(NL)
				sb.append(elm[0])
			}
		}
		props.put('di.modules', sb.toString())
		sb.setLength(0)
		DI_HANDLERS_ALL.each { elm ->
			int flag = elm[1]
			if(flag == FLAG_ALL || ((flag & flags) == flag)) {
				println "-Bootstrapper: ${elm[0]}"
				sb.append(NL)
				sb.append(elm[0])
			}
		}
		props.put('di.handlers', sb.toString())
		
		// web client caching policy
		String tkn = (this.stage == 'debug')? '' : '.js .css .gif .jpg .png'
		props.put('oneDayCacheFileExts', tkn)
		
		// read web.xml
		StringBuilder sbuf = new StringBuilder(3000)
		new File(basedir + '/src/main/webapp/WEB-INF', 'web.xml').eachLine{ line ->
			sbuf.append(rplProps(line, props))
		    sbuf.append(NL)
		}
		String s = sbuf.toString()
		
		// dao impl filtering
		println "  applying dao impl: ${daoImpl}"
		switch(daoImpl) {
			case 'db4o':
				s = s.replaceAll(regex_db_jdo, '')
				break;
			case 'jdo':
				s = s.replaceAll(regex_db_db40, '')
				break
		}
		
		// security impl filtering
		println "  applying security impl: ${securityImpl}"
		switch(securityImpl) {
			case 'none':
			s = s.replaceAll(regex_security_acegi, '')
			break
			case 'acegi':
				s = s.replaceAll(regex_security_none, '')
				break
		}
		
		s = s.replaceFirst(/(?s)<web-app>\s+/, '<web-app>' + NL + '\t')
		
		// create the resolved web.xml
		String dir = project.build.directory.toString() + '/war/WEB-INF'
		ant.mkdir(dir: dir)
		(new File(dir, 'web.xml')).write(s);
		println 'web.xml created'
	}
	
	/**
	 * Generates GWT Constants.properties file.
	 */
	public void generateGwtConstantsFile() {
	    println 'Generating GWT Constants.properties file..'
		String tgtDir = project.build.outputDirectory.toString() + '/com/tll/client'
	    ant.mkdir(dir: tgtDir)
	    def gwtProps = new HashMap();
	    gwtProps.put("appVersion", project.version)
	    def f = new File(tgtDir, 'SmbizConstants.properties')
	    f.createNewFile()
	    StringBuilder sbuf = new StringBuilder(1024)
	    gwtProps.each() {key, value -> 
	      sbuf.append(key)
	      sbuf.append(' ')
	      sbuf.append(value)
	      sbuf.append(NL)
	    }
	    f.write(sbuf.toString())
	    println f.getPath() + ' created'
	}
	
	/**
	 * Saves the generated config to disk that is deploy ready.
	 */
	public void saveConfig() {
	    // create aggregated config.properties file..
	    println 'Creating consolidated config.properties file..'
		//String tgtDir = basedir + '/target/war/WEB-INF/classes'
		String tgtDir = project.build.outputDirectory.toString()
		File f = new File(tgtDir, ConfigRef.DEFAULT_NAME)
	    config.saveAsPropFile(f)
	    println f.getPath() + ' created'
	}

	/**
	 * Stubs the app db if it doesn't exist.
	 */
	private void stubDbIfNecessary() {
		
		 // get a db shell instance
		 IDbShell dbShell = null;
		 switch(daoImpl) {
			case 'db4o':
				Config cfg = new Config();
				String db4oFilepath = project.build.outputDirectory.toString() + '/' + config.getString('db.db4o.filename')
				File f = new File(db4oFilepath);
				if(!f.exists()) {
					println 'Stubbing smbiz db4o db..'
					cfg.addProperty('db.db4o.filename', db4oFilepath);
					cfg.setProperty('db.db4o.springTransactions', false)
					dbShell = Guice.createInjector(new SmbizModelModule(), 
							new SmbizEGraphModule(), 
							new SmbizDb4oDaoModule(cfg), new Db4oDbShellModule()).getInstance(IDbShell.class);
					dbShell.restub();
				}
				break
			case 'jdo':
				// TODO implement
				throw new UnsupportedOperationException()
				/*
				String tgtDir = project.build.outputDirectory.toString()
				File fSchema = new File(tgtDir, config.getString('db.resource.schema'))
				File fStub = new File(tgtDir, config.getString('db.resource.stub'))
				File fDelete = new File(tgtDir, config.getString('db.resource.delete'))
				def shell = DbShellBuilder.getDbShell(config, 
				  fSchema.toURI().toURL(), fStub.toURI().toURL(), fDelete.toURI().toURL());
				*/
				break
		}
    }
}
