// script.js — frontend logic for Cloud-Based Blood Donor Finder

// ===============================
// 🩸 REGISTER DONOR
// ===============================
async function registerDonor(event) {
  event.preventDefault();

  const name = document.getElementById("name").value.trim();
  const bloodGroup = document.getElementById("bloodGroup").value.trim();
  const contactNumber = document.getElementById("contactNumber").value.trim();
  const latitude = parseFloat(document.getElementById("latitude").value);
  const longitude = parseFloat(document.getElementById("longitude").value);

  if (!name || !bloodGroup || !contactNumber || isNaN(latitude) || isNaN(longitude)) {
    alert("⚠️ Please fill in all required fields!");
    return;
  }

  const donorData = {
    name,
    bloodGroup,
    contactNumber,
    latitude,
    longitude
  };

  try {
    const response = await fetch("http://localhost:8080/api/donors", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(donorData)
    });

    if (response.ok) {
      const result = await response.json();
      alert(`✅ Donor Registered Successfully!\nName: ${result.name}\nBlood Group: ${result.bloodGroup}`);
      document.getElementById("registerForm").reset();
    } else {
      const errorText = await response.text();
      alert("❌ Error registering donor: " + errorText);
    }
  } catch (error) {
    console.error("Error:", error);
    alert("❌ Unable to register donor. Please try again later.");
  }
}

// ===============================
// 📍 AUTO-DETECT LOCATION
// ===============================
function getCurrentLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        document.getElementById("latitude").value = position.coords.latitude.toFixed(6);
        document.getElementById("longitude").value = position.coords.longitude.toFixed(6);
      },
      (error) => {
        console.error(error);
        alert("⚠️ Unable to retrieve your location. Please enter manually.");
      }
    );
  } else {
    alert("❌ Geolocation is not supported by your browser.");
  }
}

// ===============================
// 🔍 SEARCH DONORS
// ===============================
async function searchDonors() {
  const bloodGroup = document.getElementById("searchBloodGroup").value.trim();
  const lat = parseFloat(document.getElementById("searchLat").value);
  const lng = parseFloat(document.getElementById("searchLng").value);
  const radiusKm = parseFloat(document.getElementById("radiusKm").value);

  let url = `http://localhost:8080/api/donors?`;
  if (bloodGroup) url += `bloodGroup=${encodeURIComponent(bloodGroup)}&`;
  if (!isNaN(lat) && !isNaN(lng) && !isNaN(radiusKm)) {
    url += `lat=${lat}&lng=${lng}&radiusKm=${radiusKm}`;
  }

  try {
    const response = await fetch(url);
    const donors = await response.json();

    const resultDiv = document.getElementById("donorResults");
    resultDiv.innerHTML = "";

    if (response.ok && donors.length > 0) {
      donors.forEach((d) => {
        const div = document.createElement("div");
        div.classList.add("donor-card");
        div.innerHTML = `
          <h3>🩸 ${d.name}</h3>
          <p><b>Blood Group:</b> ${d.bloodGroup}</p>
          <p><b>Contact:</b> ${d.contactNumber}</p>
          <p><b>Location:</b> ${d.latitude}, ${d.longitude}</p>
        `;
        resultDiv.appendChild(div);
      });
    } else {
      resultDiv.innerHTML = `<p>⚠️ No donors found matching your criteria.</p>`;
    }
  } catch (error) {
    console.error("Error fetching donors:", error);
    alert("❌ Unable to search donors right now.");
  }
}

// ===============================
// 🌍 NAVIGATION
// ===============================
function goToRegister() {
  window.location.href = "/register.html";
}

function goToHome() {
  window.location.href = "/index.html";
}
