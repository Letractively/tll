This module is multi-faceted:

1. Creates tll-assemblies.jar containing assemblies that exist in src/main/resources 
   for use by all tll module projects.

2. bin dir.
   Contains shell scripts for installing tll dependencies into the local maven repo that
   are invoked by referencing them relative to the current tll module.  
   These scripts are *not* installed to the local repo.
   
3. checkstyle dir.
   Contains tll checkstyle definitions.