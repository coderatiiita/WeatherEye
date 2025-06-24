async function fetchWeather() {
    const city = document.getElementById('city').value;
    const baseUrl = window.location.origin;
    const res = await fetch(`${baseUrl}/api/weather?city=${encodeURIComponent(city)}`);
    const text = await res.text();
    document.getElementById('result').textContent = text;
}

