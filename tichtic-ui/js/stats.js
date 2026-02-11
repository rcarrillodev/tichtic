document.addEventListener('DOMContentLoaded', () => {
    const API_BASE_URL = 'http://localhost:8080';
    const statsTableBody = document.getElementById('statsTableBody');

    async function fetchStats() {
        try {
            const response = await fetch(`${API_BASE_URL}/stats`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const stats = await response.json();
            populateStatsTable(stats);
        } catch (error) {
            console.error('Error fetching stats:', error);
            alert('There was an error fetching the statistics. Please try again later.');
        }
    }

    function populateStatsTable(stats) {
        statsTableBody.innerHTML = '';
        stats.forEach(stat => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${stat.shortCode}</td>    
                <td>${stat.originalUrl}</td>            
                <td>${new Date(stat.createdAt).toLocaleDateString()}</td>
                <td>${new Date(stat.lastAccessed).toLocaleDateString()}</td>
                <td>${stat.hits}</td>
            `;
            statsTableBody.appendChild(row);
        });
    }

    fetchStats();
});