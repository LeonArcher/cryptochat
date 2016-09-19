# CryptoChat server engine (Python Flask)
## Current deployment URL
Microsoft Azure: http://crypto-chat.azurewebsites.net/
## Server API routes
* /api/packages/*receiver_id* [GET] - get all packages for specific receiver by *receiver_id*. Returns list of packages, each package contains fields: *id* (package unique number), *sender_id*, *receiver_id*, *sent_time*, *data*.
* /api/packages/ [POST] - send a new package to specific user. Fields required (strings only): *sender_id*, *receiver_id*, *data*.
