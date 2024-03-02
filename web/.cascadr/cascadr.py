import re

import sys
 
fileToUpdate = sys.argv[1]

print(fileToUpdate)

nodeOpenName="22222"

nodeCloseName="44444"

def cascadeStylingIncludes():

	templateSectionBegin="<!-- head / css / begin -->"

	templateSectionEnd="<!-- head / css / end -->"

	with open('templates/head.template') as templateFile:

		substitutionContent = templateFile.read()

	with open(fileToUpdate) as targetSubstitutionFile:
		s = targetSubstitutionFile.read()

	ddd=s

	ddd=re.sub(templateSectionBegin, templateSectionBegin+nodeOpenName, ddd, flags=re.DOTALL)

	ddd=re.sub(templateSectionEnd, nodeCloseName+templateSectionEnd, ddd, flags=re.DOTALL)

	ddd=re.sub(nodeOpenName+".*"+nodeCloseName,substitutionContent,ddd,flags=re.DOTALL)

	f = open(fileToUpdate, "w")
	
	f.write(ddd)
	
	f.close()

def cascadeJavascriptIncludes():

	templateSectionBegin="<!-- body / javascript / begin -->"

	templateSectionEnd="<!-- body / javascript / end -->"

	with open('templates/body-javascript-includes.template') as templateFile:

		substitutionContent = templateFile.read()

	with open(fileToUpdate) as targetSubstitutionFile:
		s = targetSubstitutionFile.read()

	ddd=s

	ddd=re.sub(templateSectionBegin, templateSectionBegin+nodeOpenName, ddd, flags=re.DOTALL)

	ddd=re.sub(templateSectionEnd, nodeCloseName+templateSectionEnd, ddd, flags=re.DOTALL)

	ddd=re.sub(nodeOpenName+".*"+nodeCloseName,substitutionContent,ddd,flags=re.DOTALL)

	f = open(fileToUpdate, "w")
	
	f.write(ddd)
	
	f.close()

def cascadeFooterContent():

	templateSectionBegin="<!-- Footer Start -->"

	templateSectionEnd="<!-- Footer End -->"

	with open('templates/footer.template') as templateFile:

		substitutionContent = templateFile.read()

	with open(fileToUpdate) as targetSubstitutionFile:
		s = targetSubstitutionFile.read()

	ddd=s

	ddd=re.sub(templateSectionBegin, templateSectionBegin+nodeOpenName, ddd, flags=re.DOTALL)

	ddd=re.sub(templateSectionEnd, nodeCloseName+templateSectionEnd, ddd, flags=re.DOTALL)

	ddd=re.sub(nodeOpenName+".*"+nodeCloseName,substitutionContent,ddd,flags=re.DOTALL)

	f = open(fileToUpdate, "w")
	
	f.write(ddd)
	
	f.close()

def cascadeTopNavigation():

	templateSectionBegin="<!-- Navbar -->"

	templateSectionEnd=" <!-- /.navbar -->"

	with open('templates/navigation-top-menu.template') as templateFile:

		substitutionContent = templateFile.read()

	with open(fileToUpdate) as targetSubstitutionFile:
		s = targetSubstitutionFile.read()

	ddd=s

	ddd=re.sub(templateSectionBegin, templateSectionBegin+nodeOpenName, ddd, flags=re.DOTALL)

	ddd=re.sub(templateSectionEnd, nodeCloseName+templateSectionEnd, ddd, flags=re.DOTALL)

	ddd=re.sub(nodeOpenName+".*"+nodeCloseName,substitutionContent,ddd,flags=re.DOTALL)

	f = open(fileToUpdate, "w")
	
	f.write(ddd)
	
	f.close()

def cascadeSideNavigation():

	templateSectionBegin="<!-- Sidebar Start -->"

	templateSectionEnd="<!-- Sidebar End -->"

	with open('templates/side-navigation.template') as templateFile:

		substitutionContent = templateFile.read()

	with open(fileToUpdate) as targetSubstitutionFile:
		s = targetSubstitutionFile.read()

	ddd=s

	ddd=re.sub(templateSectionBegin, templateSectionBegin+nodeOpenName, ddd, flags=re.DOTALL)

	ddd=re.sub(templateSectionEnd, nodeCloseName+templateSectionEnd, ddd, flags=re.DOTALL)

	ddd=re.sub(nodeOpenName+".*"+nodeCloseName,substitutionContent,ddd,flags=re.DOTALL)

	f = open(fileToUpdate, "w")
	
	f.write(ddd)
	
	f.close()

def cascadeFlyoutContent():

	templateSectionBegin="<!-- body / flyout / begin -->"

	templateSectionEnd="<!-- body / flyout / end -->"

	with open('templates/body-flyout-content.template') as templateFile:

		substitutionContent = templateFile.read()

	with open(fileToUpdate) as targetSubstitutionFile:
		s = targetSubstitutionFile.read()

	ddd=s

	ddd=re.sub(templateSectionBegin, templateSectionBegin+nodeOpenName, ddd, flags=re.DOTALL)

	ddd=re.sub(templateSectionEnd, nodeCloseName+templateSectionEnd, ddd, flags=re.DOTALL)

	ddd=re.sub(nodeOpenName+".*"+nodeCloseName,substitutionContent,ddd,flags=re.DOTALL)

	f = open(fileToUpdate, "w")
	
	f.write(ddd)
	
	f.close()

###################################################################
##
## driver
##
###################################################################

cascadeStylingIncludes()

cascadeSideNavigation()

#cascadeJavascriptIncludes()

cascadeFooterContent()

#cascadeTopNavigation()



#cascadeFlyoutContent()
##updateSideBarDesktop()

##updateSideBarMobile()
