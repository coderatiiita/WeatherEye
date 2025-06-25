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
    const html = `
        <h2>${data.resolvedAddress || ''}</h2>
        <p><strong>Conditions:</strong> ${current.conditions || day?.description || 'N/A'}</p>
        <p><strong>Temperature:</strong> ${current.temp ?? day?.temp} °C</p>
        <p><strong>Humidity:</strong> ${current.humidity ?? day?.humidity}%</p>
        <p><strong>Wind:</strong> ${current.windspeed ?? day?.windspeed} km/h</p>
    `;
    document.getElementById('result').innerHTML = html;
}

