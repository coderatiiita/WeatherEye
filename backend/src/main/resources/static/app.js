async function fetchWeather() {
    const city = document.getElementById('city').value;
    const res = await fetch(`/api/weather?city=${encodeURIComponent(city)}`);
    const text = await res.text();
    document.getElementById('result').textContent = text;
}
