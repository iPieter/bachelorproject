# REST authentication

# Obtaining a login token
The user can obtain a token by making a ```POST```-request to the following url:
```
$(base_url)/rest/login
```

with two request body parameters:
- The username as ```email```.
- The password as ```password```.

If the login is correct, the return body will be something like:
```(json)
{
"id": 76,
"owner": {
"email": "john3@test.be",
"id": 44,
"lastPrettyLogin": "een ogenblik geleden",
"imageHash": "qwertyui",
"name": "John Doe",
"role": "MECHANIC"
},
"token": "PiKeoTAzP0+gVSBNAxqCNpqLpI87ohR298LLf4SP09YtDNiDYTAwCZ4M7sfkVmOLiobnTXsF3AvfeqbzAJZc0TGlWcZgkOq63sgtEG41h3b4/nFsG/tUAjKgbNSxTml3NJ3A3DLP9HJuCRFXJPWv9/PKA5vKOvQibog6ZdYbsx9VFpH71g1/ZJ1K176q0uP+Ot8MEySihBr8vnye4jRYgGEbL/XKL8qYNQOZkpdMVyN0XhJ9mwqw6m2LAvRpFwLv"
}
```
Notice the token id is returned, as well as the user id of the owner. 

If the login is incorrect, the service will return:
```
HTTP 401 Unauthorized
```

## Using the token
You can use the token on the secured api endpoints by including the ```Autorization``` paramater in your request header. It's important to notice we use the header, not the body.

Your header value consists of ```Bearer $(token)```, so it might look something like this:
```
Bearer PiKeoTAzP0+gVSBNAxqCNpqLpI87ohR298LLf4SP09YtDNiDYTAwCZ4M7sfkVmOLiobnTXsF3AvfeqbzAJZc0TGlWcZgkOq63sgtEG41h3b4/nFsG/tUAjKgbNSxTml3NJ3A3DLP9HJuCRFXJPWv9/PKA5vKOvQibog6ZdYbsx9VFpH71g1/ZJ1K176q0uP+Ot8MEySihBr8vnye4jRYgGEbL/XKL8qYNQOZkpdMVyN0XhJ9mwqw6m2LAvRpFwLv
```

If succesful, you should see a ```HTTP 200``` response.

## Token expiration
The tokens will last 14 days by default.
