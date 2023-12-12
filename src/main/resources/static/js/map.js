fetch('/js/provinces.geojson')
    .then(response => {
        // Check if the request was successful (status code 200)
        if (!response.ok) {
            throw new Error(`Failed to fetch GeoJSON file (status ${response.status})`);
        }
        // Parse the response as JSON
        return response.json();
    })
    .then(geojsonData => {
        // Handle the GeoJSON data here
        const geoJson = geojsonData;
        var southWest = L.latLng(41.2354, 22.3597);
        var northEast = L.latLng(44.2148, 28.6070);
        var bounds = L.latLngBounds(southWest, northEast);

        let mapDiv = document.getElementById("map");

        var map = L.map(mapDiv, {
            center: [42.7000, 25.4858],
            zoom: 7.45,
            maxBounds: bounds,
            dragging: true
        });

        var provincesLayer = L.geoJSON(geoJson, {
            onEachFeature: function (feature, layer) {
                layer.on('mouseover', function (e) {
                    // Change color on hover
                    layer.setStyle({
                        fillColor: 'yellow',
                        fillOpacity: 0.7
                    });
                    console.log(feature.properties.nuts3);
                });

                layer.on('mouseout', function (e) {
                    // Reset color on mouseout
                    layer.setStyle({
                        fillColor: '#BBCCE4', // Change this to the original color
                        fillOpacity: 0.5 // Change this to the original opacity
                    });
                });

                // Add click event listener
                layer.on('click', function (e) {
                    test(feature);
                });
            }
        }).addTo(map);

        map.setMaxBounds(bounds);

    })
    .catch(error => {
        // Handle any errors that occurred during the fetch
        console.error('Error fetching GeoJSON file:', error);
    });

