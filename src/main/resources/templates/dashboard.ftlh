<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
        <div class="col-md-3 mb-2 mb-md-0">
            <a href="/" class="d-inline-flex link-body-emphasis text-decoration-none">
                <svg class="bi" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"></use></svg>
            </a>
        </div>

        <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
            <li><a href="/" class="nav-link px-2 link-secondary">Main</a></li>
            <li><a href="/dashboard" class="nav-link px-2 ">Dashboard</a></li>
            <li><a href="/services" class="nav-link px-2 ">Services</a></li>
        </ul>
    </header>
</div>

<div class="container">
    <h1>Dashboard</h1>
    <div>
        <canvas id="apiChart" width="400" height="200"></canvas>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Function to fetch API data from Spring backend
    async function fetchApiData() {
        try {
            const response = await fetch('/apis/all'); // Replace with your actual API endpoint
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Error fetching API data:', error);
        }
    }

    // Function to update the chart with fetched data
    async function updateChart() {
        const apiData = await fetchApiData();

        if (apiData) {
            const apiNames = apiData.map(api => api.name);
            const apiRequests = apiData.map(api => api.responseCode);

            new Chart("apiChart", {
                type: 'bar',
                data: {
                    labels: apiNames,
                    datasets: [{
                        label: 'Response codes',
                        data: apiRequests,
                        backgroundColor: 'rgb(146, 101, 178)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        updateChart();
    });
</script>
</body>
</html>
