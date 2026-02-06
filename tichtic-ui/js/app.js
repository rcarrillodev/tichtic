document.addEventListener('DOMContentLoaded', () => {
    const urlForm = document.getElementById('urlForm');
    const resultCard = document.getElementById('result');
    const shortUrlInput = document.getElementById('shortUrl');
    const copyButton = document.getElementById('copyButton');
    const openLinkButton = document.getElementById('openLink');
    const API_BASE_URL = 'http://localhost:8080'; 
    urlForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const longUrl = document.getElementById('longUrl').value;
        
        try {
            const response = await fetch(`${API_BASE_URL}?url=${encodeURIComponent(longUrl)}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            const shortUrl = `${data.shortenedUrl}`;

            shortUrlInput.value = shortUrl;
            resultCard.classList.remove('d-none');
        } catch (error) {
            console.error('Error:', error);
            alert('There was an error shortening your URL. Please try again.');
        }
    });

    copyButton.addEventListener('click', async () => {
        try {
            await navigator.clipboard.writeText(shortUrlInput.value);
            copyButton.textContent = 'Copied!';
            copyButton.classList.add('copy-success');
            
            setTimeout(() => {
                copyButton.textContent = 'Copy';
                copyButton.classList.remove('copy-success');
            }, 2000);
        } catch (err) {
            console.error('Failed to copy text: ', err);
        }
    });

    openLinkButton.addEventListener('click', () => {
        const url = shortUrlInput.value;
        if (url) {
            window.open(url, '_blank');
        }
    });
});
