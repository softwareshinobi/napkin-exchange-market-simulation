# fixes

refresh page on project change from flyout menu

the default project isn't setting if there is a project available

story id and story project not loading in 'all stories' page

integrate status monitoring in the menu

integrate status monotiring on dedicated page

on story delete, return to kanban

on project delete, return to project list

on new story create, allow ability to navigate or stay

on new project create, display project list

integrate full calendar

integrate project wide targets


## known bugs

the kanban board matching is using the project string name. this will break the kanban board when the project name is updated. use the id. i didn't b/c time.

## todo

add logback.xml with shorter terminal output

add in a server side 404 page

add in a server side 500 page

add a mapping for /error

get a diagram server running using a reverse proxy situation over docker compose for (diagram. & draw.)

