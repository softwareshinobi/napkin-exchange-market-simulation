## Things To Do List  (v.0.1)

This document outlines a list of outstanding tasks and improvements for this project. Feel free to contribute to this list or mark completed items as you progress through development.

**High Priority:**

* **User Management:**
    * **System User Creation:** Implement logic to create a system user on boot if one doesn't already exist. (**Security Task**)
    * **Mateo User Creation:** Implement logic to create a user named "Mateo" on boot if the user doesn't already exist. (**User Management Task**)
* **System Configuration:**
    * **Significant Digits:** Increase the precision of numerical calculations within the system for improved accuracy. (**System Optimization**)
    * **Random Number Seeding:** Configure a mechanism for seeding the random number generator for predictable behavior during testing. (**Testing Improvement**)

**Medium Priority:**

* **Dependencies:**
    * **Links Import:** Review the import statement for "links from worksace" and ensure it references the correct dependency. (**Dependency Management Task**)
    * **Docs Import:** Clarify the purpose of the "import docs" statement and address any potential issues. (**Dependency Management Task**)
    * **Build Situation:** Investigate the purpose and functionality of "import build situation." Refactor or remove if no longer needed. (**Dependency Management Task**)

**Low Priority:**

* **UI/UX Enhancements:**
    * **Login Notification:** Replace the JavaScript alert displayed after user login with a styled Bootstrap modal for a more user-friendly experience. (**UI/UX Improvement**)
    * **Primary Dashboard:** Update the primary dashboard to replace the hoverable table with a carousel for improved visual appeal. (**UI/UX Improvement**)

**Trading Functionality:**

* **Real-time Updates:**
    * **Live Pricing:** Implement real-time price updates for bought and sold assets to provide users with accurate valuations. (**High Priority Feature**)
    * **Total Order Pricing:** Introduce real-time updates for total order pricing to reflect dynamic market changes. (**High Priority Feature**)
* **Order Management:**
    * **Traditional Order Types:** Integrate traditional order types beyond the current system  for user flexibility. (**High Priority Feature**)
    * **Custom Buy/Sell Stops:** Allow users to define their own buy stop and take profit values for greater control. (**High Priority Feature**)
    * **Order Cancellation:** Fix the issue where cancelled orders remain listed as "processing." (**Bug Fix**)

**Notifications:**

* **Time Tracking:** Implement time tracking for key events within the system to facilitate proper notification timing. (**Feature Implementation**)
* **Notification Content:** Review and correct any errors or inconsistencies within limit order notification messages. (**UI/UX Improvement**)
* **Notification Integration:** Reintegrate notification functionality back into the menu and live feed for a seamless user experience. (**UI/UX Improvement**)
