$(document).ready(function () {

    mapboxgl.accessToken = 'pk.eyJ1IjoieWNhbGlzYXIiLCJhIjoiY2p3OWRpZzdlMjVtejRib3czYmJhejA0NiJ9.mwXEh7IU5of_tqY3syx-WA';
    var map = new mapboxgl.Map({
        container: 'map',
        style: 'mapbox://styles/mapbox/streets-v9',
        center: [-103.59179687498357, 40.66995747013945],
        zoom: 3
    });

    map.on('load', function() {
// Add a new source from our GeoJSON data and set the
// 'cluster' option to true. GL-JS will add the point_count property to your source data.
        map.addSource("earthquakes", {
            type: "geojson",
// Point to GeoJSON data. This example visualizes all M1.0+ earthquakes
// from 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
            data: "https://docs.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson",
            cluster: true,
            clusterMaxZoom: 14, // Max zoom to cluster points on
            clusterRadius: 50 // Radius of each cluster when clustering points (defaults to 50)
        });

        map.addLayer({
            id: "clusters",
            type: "circle",
            source: "earthquakes",
            filter: ["has", "point_count"],
            paint: {
// Use step expressions (https://docs.mapbox.com/mapbox-gl-js/style-spec/#expressions-step)
// with three steps to implement three types of circles:
//   * Blue, 20px circles when point count is less than 100
//   * Yellow, 30px circles when point count is between 100 and 750
//   * Pink, 40px circles when point count is greater than or equal to 750
                "circle-color": [
                    "step",
                    ["get", "point_count"],
                    "#ff0000",
                    100,
                    "#add8e6",
                    750,
                    "#228B22"
                    // "
                ],
                "circle-radius": [
                    "step",
                    ["get", "point_count"],
                    20,
                    100,
                    30,
                    750,
                    40
                ]
            }
        });

        map.addLayer({
            id: "cluster-count",
            type: "symbol",
            source: "earthquakes",
            filter: ["has", "point_count"],
            layout: {
                "text-field": "{point_count_abbreviated}",
                "text-font": ["DIN Offc Pro Medium", "Arial Unicode MS Bold"],
                "text-size": 12
            }
        });

        map.addLayer({
            id: "unclustered-point",
            type: "circle",
            source: "earthquakes",
            filter: ["!", ["has", "point_count"]],
            paint: {
                "circle-color": "#11b4da",
                "circle-radius": 4,
                "circle-stroke-width": 1,
                "circle-stroke-color": "#fff"
            }
        });

// inspect a cluster on click
        map.on('click', 'clusters', function (e) {
            var features = map.queryRenderedFeatures(e.point, { layers: ['clusters'] });
            var clusterId = features[0].properties.cluster_id;
            map.getSource('earthquakes').getClusterExpansionZoom(clusterId, function (err, zoom) {
                if (err)
                    return;

                map.easeTo({
                    center: features[0].geometry.coordinates,
                    zoom: zoom
                });
            });
        });

        map.on('mouseenter', 'clusters', function () {
            map.getCanvas().style.cursor = 'pointer';
        });
        map.on('mouseleave', 'clusters', function () {
            map.getCanvas().style.cursor = '';
        });
    });

});



// $(document).ready(function () {
//
//     mapboxgl.accessToken = 'pk.eyJ1IjoieWNhbGlzYXIiLCJhIjoiY2p3OWRpZzdlMjVtejRib3czYmJhejA0NiJ9.mwXEh7IU5of_tqY3syx-WA';
//
//     console.log("loading map");
//     var map = new mapboxgl.Map({
//         container: 'map',
//         style: 'mapbox://styles/mapbox/streets-v9',
//         center: [-87.65, 41.85],
//         zoom: 4
//     });
//
//
//     var green = "124,252,0";
//     var red = "255, 200, 200";
//     var blue = "65,105,225";
//     var arr = [green, red, blue];
//     var color = arr[Math.floor(Math.random()*arr.length)];
//
//     // var pulsingDot = {
//     //     width: size,
//     //     height: size,
//     //     data: new Uint8Array(size * size * 4),
//     //
//     //     onAdd: function() {
//     //         var canvas = document.createElement('canvas');
//     //         canvas.width = this.width;
//     //         canvas.height = this.height;
//     //         this.context = canvas.getContext('2d');
//     //     },
//     //
//     //     render: function() {
//     //         var duration = 1000;
//     //         var t = (performance.now() % duration) / duration;
//     //
//     //         var radius = size / 2 * 0.3;
//     //         var outerRadius = size / 2 * 0.7 * t + radius;
//     //         var context = this.context;
//     //
//     //         // draw outer circle
//     //         context.clearRect(0, 0, this.width, this.height);
//     //         context.beginPath();
//     //         context.arc(this.width / 2, this.height / 2, outerRadius, 0, Math.PI * 2);
//     //         context.fillStyle = 'rgba(' + color + ',' + (1 - t) + ')';
//     //         context.fill();
//     //
//     //         // draw inner circle
//     //         context.beginPath();
//     //         context.arc(this.width / 2, this.height / 2, radius, 0, Math.PI * 2);
//     //         context.fillStyle = 'rgba(' + color + ', 1)';
//     //         context.strokeStyle = 'white';
//     //         context.lineWidth = 2 + 4 * (1 - t);
//     //         context.fill();
//     //         context.stroke();
//     //
//     //         // update this image's data with data from the canvas
//     //         this.data = context.getImageData(0, 0, this.width, this.height).data;
//     //
//     //         // keep the map repainting
//     //         map.triggerRepaint();
//     //
//     //         // return `true` to let the map know that the image was updated
//     //         return true;
//     //     }
//     // };
//
//     map.on('load', function () {
//
//         map.addImage('green-dot', buildDot(map, green), { pixelRatio: 2 });
//
//         map.addImage('red-dot', buildDot(map, red), { pixelRatio: 2 });
//
//         map.addLayer({
//             "id": "green-points",
//             "type": "symbol",
//             "source": {
//                 "type": "geojson",
//                 "data": {
//                     "type": "FeatureCollection",
//                     "features": [{
//                         "type": "Feature",
//                         "geometry": {
//                             "type": "Point",
//                             "coordinates": [-87.65, 41.85]
//                         }
//                     }]
//                 }
//             },
//             "layout": {
//                 "icon-image": "green-dot"
//             }
//         });
//
//
//
//
//         map.addLayer({
//             "id": "red-points",
//             "type": "symbol",
//             "source": {
//                 "type": "geojson",
//                 "data": {
//                     "type": "FeatureCollection",
//                     "features": [{
//                         "type": "Feature",
//                         "geometry": {
//                             "type": "Point",
//                             "coordinates": [-86.65, 41.85]
//                         }
//                     }]
//                 }
//             },
//             "layout": {
//                 "icon-image": "green-dot"
//             }
//         });
//
//     });
//
//     //
//     //     map.addImage('pulsing-dot', pulsingDot, { pixelRatio: 2 });
//     //
//     //     var features = [];
//     //     for(var i = 0; i < starbucks.length; i++){
//     //         var s = starbucks[i];
//     //         // console.log()
//     //         var lon = s[14]
//     //         var lat = s[15];
//     //         var name = s[2]
//     //         var brandName = s[3];
//     //         features.push({
//     //             type: "Feature",
//     //             geometry: {
//     //                 "type": "Point",
//     //                 "coordinates": [lon, lat]
//     //             }
//     //         })
//     //     }
//     //
//     //     // map.addLayer({
//     //     //     "id": "points",
//     //     //     "type": "symbol",
//     //     //     "source": {
//     //     //         "type": "geojson",
//     //     //         "data": {
//     //     //             "type": "FeatureCollection",
//     //     //             "features": features
//     //     //             //     [
//     //     //             //     {
//     //     //             //     "type": "Feature",
//     //     //             //     "geometry": {
//     //     //             //         "type": "Point",
//     //     //             //         "coordinates": [35.217018, 31.771959]
//     //     //             //     }
//     //     //             // },
//     //     //             // {
//     //     //             //     "type": "Feature",
//     //     //             //     "geometry": {
//     //     //             //         "type": "Point",
//     //     //             //         "coordinates": [34.855499, 32.109333]
//     //     //             //     }
//     //     //             // }
//     //     //             // ]
//     //     //         }
//     //     //     },
//     //     //     "layout": {
//     //     //         "icon-image": "pulsing-dot"
//     //     //     }
//     //     // });
//     //     //
//     //     // map.addLayer({
//     //     //     "id": "points",
//     //     //     "type": "symbol",
//     //     //     "source": {
//     //     //         "type": "geojson",
//     //     //         "data": {
//     //     //             "type": "FeatureCollection",
//     //     //             "features": [
//     //     //                 {
//     //     //                 "type": "Feature",
//     //     //                 "geometry": {
//     //     //                     "type": "Point",
//     //     //                     "coordinates": [34.855499, 32.109333]
//     //     //                 }
//     //     //             }
//     //     //             ]
//     //     //         }
//     //     //     },
//     //     //     "layout": {
//     //     //         "icon-image": "pulsing-dot"
//     //     //     }
//     //     // });
//     //
//     // });
// });
//
// function buildDot(map, color){
//     var size = 200;
//
//     var dot = {
//         width: size,
//         height: size,
//         data: new Uint8Array(size * size * 4),
//
//         onAdd: function() {
//             var canvas = document.createElement('canvas');
//             canvas.width = this.width;
//             canvas.height = this.height;
//             this.context = canvas.getContext('2d');
//         },
//
//         render: function() {
//             var duration = 1000;
//             var t = (performance.now() % duration) / duration;
//
//             var radius = size / 2 * 0.3;
//             var outerRadius = size / 2 * 0.7 * t + radius;
//             var context = this.context;
//
// // draw outer circle
//             context.clearRect(0, 0, this.width, this.height);
//             context.beginPath();
//             context.arc(this.width / 2, this.height / 2, outerRadius, 0, Math.PI * 2);
//             context.fillStyle = 'rgba(' + color + ',' + (1 - t) + ')';
//             context.fill();
//
// // draw inner circle
//             context.beginPath();
//             context.arc(this.width / 2, this.height / 2, radius, 0, Math.PI * 2);
//             context.fillStyle = 'rgba(' + color + ', 1)';
//             context.strokeStyle = 'white';
//             context.lineWidth = 2 + 4 * (1 - t);
//             context.fill();
//             context.stroke();
//
// // update this image's data with data from the canvas
//             this.data = context.getImageData(0, 0, this.width, this.height).data;
//
// // keep the map repainting
//             map.triggerRepaint();
//
// // return `true` to let the map know that the image was updated
//             return true;
//         }
//     };
//
//     return dot;
//
// }
