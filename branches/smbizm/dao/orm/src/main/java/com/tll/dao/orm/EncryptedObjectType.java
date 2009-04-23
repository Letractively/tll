package com.tll.dao.orm;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import com.tll.util.CryptoUtil;

/**
 * EncryptedObjectType - Encrypts/decrypts {@link Serializable} objects.
 * @author jpk
 */
public class EncryptedObjectType implements UserType {

	public int[] sqlTypes() {
		return new int[] { Types.BINARY };
	}

	@SuppressWarnings("unchecked")
	public Class returnedClass() {
		return byte[].class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if(x == y) {
			return true;
		}
		if(null == x || null == y) {
			return false;
		}
		return x.equals(y);
	}

	public int hashCode(Object x) throws HibernateException {
		if(x == null) {
			return 0;
		}
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		final byte[] bytes = rs.getBytes(names[0]);
		if(bytes == null) {
			return null;
		}
		try {
			return CryptoUtil.decryptSerializable(bytes);
		}
		catch(final GeneralSecurityException e) {
			throw new HibernateException("Unable to decrypt object due to a general security exception", e);
		}
		catch(final ClassNotFoundException e) {
			throw new HibernateException("Unable to decrypt object: The object's type was not identifiable", e);
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if(value != null) {
			if(value instanceof Serializable == false) {
				throw new HibernateException("Unable to set object: It is not serializable.");
			}
			try {
				st.setBytes(index, CryptoUtil.encryptSerializable((Serializable) value));
			}
			catch(final GeneralSecurityException e) {
				throw new HibernateException("Unable to encrypt object due to a general security exception", e);
			}
		}
		else {
			st.setNull(index, Types.BINARY);
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

}
