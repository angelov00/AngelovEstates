document.addEventListener("DOMContentLoaded", function () {
    // Fetch all reviews
    fetch('/api/reviews/all')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            displayReviews(data);
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
});

function displayReviews(reviews) {

    const container = document.getElementById('reviewsContainer');
    container.innerHTML = `<h2 class="mb-4">Reviews</h2>`;

    reviews.forEach(review => {
        container.innerHTML += `
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title text-primary">User: ${review.author}</h5>
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <p class="card-text mb-0 mr-2">
                            <span class="text-warning">&#9733;</span> Rating: ${review.rating}
                        </p>
                    </div>
                    <p class="card-text">${review.comment}</p>
                </div>
            </div>
        `;
    });
}