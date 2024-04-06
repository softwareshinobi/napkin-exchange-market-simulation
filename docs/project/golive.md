## future activities / the napkin exchange (portfolio golive)

This document outlines a list of outstanding tasks and improvements for this project. Feel free to contribute to this list or mark completed items as you progress through development.

**High Priority:**

* **Order Management:**
    * **Stop-Loss Functionality:** Ensure take profit operations correctly cancel stop-loss orders to prevent unintended trades. (**Bug Fix**)
    * **Cost Basis Calculation:** Implement logic to calculate and store the cost basis for stocks owned by the logged-in user. (**Feature Implementation**)
    * **Market Buy Notification:** Enhance market buy fulfillment notifications to include the purchase price for transparency. (**UI/UX Improvement**)
    * **Sell Stop Logging:** Refine activity logs to accurately reflect sell stop actions. (**Data Integrity Fix**)
* **UI/UX Enhancements:**
    * **Market Watch Formatting:** Correct the number format for values displayed in the market watch dashboard table for better readability. (**UI Fix**)
    * **Account Profile Design:** Design and implement an account profile page showcasing key user data and relevant information. (**UI/UX Design**)
    * **Leaderboard Dates:** Address any date inconsistencies within the profile leaderboard. (**Data Accuracy Fix**)

**Medium Priority:**

* **System Monitoring:**
    * **Connected Service Monitor:** Develop a monitor to display the status of upstream content from connected services. (**Feature Implementation**)
    * **Upstream Server Configuration:** Allow users to define an upstream server within application settings. (**Feature Implementation**)
* **Dashboard & Notifications:**
    * **System Time Display:** Integrate system time display within the dashboard for user reference. (**UI/UX Enhancement**)
    * **Notification Time Zone:** Configure all notifications to utilize the system time by default for consistency. (**UI/UX Improvement**)

**Low Priority:**

* **Trading Functionality:**
    * **Sell Button:** Disable the sell button until the functionality is fully implemented. (**Temporary Fix**)
    * **Market Sell:** Implement a standard market sell operation for user convenience. (**Feature Implementation**)

**Deployment:**

* **Docker Image Update:** Update both the Docker image and Dockerfile to reflect any recent code changes and naming conventions. (**Deployment Task**)
* **API Testing:** Conduct thorough testing of the Oanda API within both production and test environments. (**Testing Task**)

**Miscellaneous:**

* **System Start Notification:**  Include a system startup notification within the application. (**UI/UX Enhancement**)
* **Time Adjustment:** While not critical, consider revising the default system time by 42 years (humorously referencing Douglas Adams' "The Hitchhiker's Guide to the Galaxy"). (**Optional Enhancement**)
