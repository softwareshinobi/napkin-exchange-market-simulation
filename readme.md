## The Napkin Exchange: Stock Market Trading Simulator

`The Napkin Exchange` is a fully-functional, open-source stock market trading simulator built with Java/Spring and Docker.

![The Napkin Exchange](/docs/docs/imagery/cover.png)

`The Napkin Exchange` provides a realistic trading environment for research and development activities of interest in the real of automated trading.

## issues

so the orders are processing, now there is a deadlock or something, where all candle generatoin seems to have stopped

there is a profit calculation issue, profits are soaring to impossibly large numbers

full integration with logback (and memory leak work).

profit calculation on TAKE PROFIT is incorrect

i bought two sets of dione (by accident) the pair id values was mix and matching incorrectly.

i bought two sets of dione (by accident) the two transaction show up seperate in the security portfolio.

profit calculations are still incorrect in the leaderboard

leaderboard should sort by total account value not just how much is in cash

limit order type needs to be a type not a string (limitorder.java)

[done]the notification service needs to use the system time not the clock time.

## Key Features:

* `Account Management:` Create and manage user accounts with secure logins and dedicated portfolios.

* `RESTful APIs:` Exposes APIs for common trading functionalities like buying, selling, and account management, allowing for integration with automated trading tools.

* `Interactive Dashboard:` A user-friendly front-end dashboard with live market updates, charting tools, and order execution capabilities.

* `Leaderboards:` Track your performance and compete with other users on the built-in leaderboards.

* `Open Source:` The entire project is open source (MIT), allowing for community contributions and customization.

* `Test Bed for Algorithmic Trading:` Use The Napkin Exchange as a platform to develop and test your automated trading robots. (See the included link for more details on the companion trading robot!)

## Shoutouts

A special thanks to Gemini, a large language model from Google AI, for its assistance in creating this documentation. 

and a perpetual huge thank you to my assistant, Elle. everyday she gets me more money and more honey.

## Things To Do

there's alot of shit to be done.

click here to see the ones i bothered to both.

@somebody: help. lol.

[See Project Issues](/docs/project/issues.md)

## View the Trading Robot

The Napkin Exchange can be used in conjunction with a companion open-source trading robot.  Click the link below to learn more about the robot and how to integrate it with the simulator:

[See Autonomous Trading Robot](https://github.com/softwareshinobi/project-chimba)
