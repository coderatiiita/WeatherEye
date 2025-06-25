async function fetchWeather() {
    const city = document.getElementById('city').value.trim();
    if (!city) {
        return;
    }
    const baseUrl = window.location.origin;
    const res = await fetch(`${baseUrl}/api/weather?city=${encodeURIComponent(city)}`);
    if (!res.ok) {
        document.getElementById('result').textContent = 'Failed to fetch weather data.';
        return;
    }
    const data = await res.json();
    displayWeather(data);
}

function displayWeather(data) {
    if (!data) return;
    const day = data.days && data.days[0];
    const current = data.currentConditions || {};
    const icon = current.icon || day?.icon || '';
    const conditions = current.conditions || day?.description || 'N/A';
    const html = `
        <h2>${data.resolvedAddress || ''}</h2>
        <p><strong>Conditions:</strong> ${conditions}</p>
        <p><strong>Temperature:</strong> ${current.temp ?? day?.temp} °C</p>
        <p><strong>Humidity:</strong> ${current.humidity ?? day?.humidity}%</p>
        <p><strong>Wind:</strong> ${current.windspeed ?? day?.windspeed} km/h</p>
    `;
    document.getElementById('result').innerHTML = html;
    updateVisuals(icon);
}

function updateVisuals(icon) {
    const iconEl = document.getElementById('weather-icon');
    if (icon) {
        iconEl.src = `https://raw.githubusercontent.com/visualcrossing/WeatherIcons/master/PNG/1st%20Set%20-%20Color/${icon}.png`;
        iconEl.style.display = 'block';
    } else {
        iconEl.style.display = 'none';
    }

    document.body.className = '';
    if (icon) {
        document.body.classList.add(`weather-${icon}`);
    }
}

