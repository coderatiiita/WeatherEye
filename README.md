# WeatherEye
https://roadmap.sh/projects/weather-api-wrapper-service


In this project, instead of relying on our own weather data, we will build a weather API that fetches and returns weather data from a 3rd party API. This project will help you understand how to work with 3rd party APIs, caching, and environment variables.

Weather API

As for the actual weather API to use, you can use your favorite one, as a suggestion, here is a link to Visual Crossing’s API, it’s completely FREE and easy to use.
https://www.visualcrossing.com/weather-api/

Regarding the in-memory cache, a pretty common recommendation is to use Redis, you can read more about it here, and as a recommendation, you could use the city code entered by the user as the key, and save there the result from calling the API.
![image](https://github.com/user-attachments/assets/fcef24c0-a7ea-4f4e-b879-03e7d0dd938a)


At the same time, when you “set” the value in the cache, you can also give it an expiration time in seconds (using the EX flag on the SET command). That way the cache (the keys) will automatically clean itself when the data is old enough (for example, giving it a 12-hours expiration time).

Some Tips
Here are some tips to help you get started:

Start by creating a simple API that returns a hardcoded weather response. This will help you understand how to structure your API and how to handle requests.
Use environment variables to store the API key and the Redis connection string. This way, you can easily change them without having to modify your code.
Make sure to handle errors properly. If the 3rd party API is down, or if the city code is invalid, make sure to return the appropriate error message.
Use some package or module to make HTTP requests e.g. if you are using Node.js, you can use the axios package, if you are using Python, you can use the requests module. The backend in this repository uses the Retrofit library for REST calls.
Implement rate limiting to prevent abuse of your API. You can use a package like express-rate-limit if you are using Node.js or flask-limiter if you are using Python.
This project will help you understand how to work with 3rd party APIs, caching, and environment variables. It will also help you understand how to structure your API and how to handle requests.

## Running the Application

The backend is a Spring Boot application located in the `backend` directory. It depends on Redis and a weather API key. Provide these values using environment variables:

```
export WEATHER_API_KEY=your_api_key
export WEATHER_API_URL=https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline
export REDIS_HOST=localhost
export REDIS_PORT=6379
export WEATHER_CACHE_TTL_SECONDS=43200 # optional cache TTL
export SERVER_PORT=8080
```

If Redis cannot be reached at runtime, the application logs a warning and still
serves data directly from the weather service without caching.

Run the application using Maven:

```
cd backend
mvn spring-boot:run
```

The UI is served from the same server port (default `8080`) so you can open
`http://localhost:$SERVER_PORT` in your browser (substitute your configured
`SERVER_PORT` value if different).
