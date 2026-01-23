document.addEventListener("DOMContentLoaded", () => {

  const listViewBtn = document.getElementById("listViewBtn");
  const calendarViewBtn = document.getElementById("calendarViewBtn");

  const listViewSection = document.getElementById("listViewContent");
  const calendarViewSection = document.getElementById("calendarViewContent");

  const calendarTitle = document.getElementById("calendarTitle");
  const calendarGrid = document.getElementById("calendarGrid");
  const prevMonthBtn = document.getElementById("prevMonth");
  const nextMonthBtn = document.getElementById("nextMonth");

  if (!listViewBtn || !calendarViewBtn) return;

  let allPosts = []; // Store posts globally

  // Function to fetch posts
  async function fetchAndRenderCalendar() {
    try {
      const response = await fetch('http://localhost:8082/api/posts/all', {
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('authToken') }
      });
      allPosts = await response.json();
      renderCalendar(allPosts);
    } catch (error) {
      console.error("Error loading posts for calendar:", error);
      renderCalendar([]);
    }
  }

  // ===== VIEW TOGGLE =====
  listViewBtn.onclick = () => {
    listViewSection.classList.remove("hidden");
    calendarViewSection.classList.add("hidden");

    listViewBtn.classList.add("bg-white", "text-blue-600", "shadow");
    calendarViewBtn.classList.remove("bg-white", "text-blue-600", "shadow");
  };

  calendarViewBtn.onclick = async () => {
    calendarViewSection.classList.remove("hidden");
    listViewSection.classList.add("hidden");

    calendarViewBtn.classList.add("bg-white", "text-blue-600", "shadow");
    listViewBtn.classList.remove("bg-white", "text-blue-600", "shadow");

    // Fetch posts and render calendar
    try {
      const response = await fetch('http://localhost:8082/api/posts/all', {
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('authToken') }
      });
      const posts = await response.json();
      renderCalendar(posts);
    } catch (error) {
      console.error("Error loading posts for calendar:", error);
      renderCalendar([]);
    }
  };

  // ===== CALENDAR LOGIC =====
  const scheduledPosts = {
    "2025-01-05": 2,
    "2025-01-10": 1,
    "2025-01-18": 3
  };

  let currentDate = new Date();
function renderCalendar(posts = []) {
  calendarGrid.innerHTML = "";

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  calendarTitle.textContent =
    currentDate.toLocaleString("default", { month: "long" }) + " " + year;

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  // Empty cells
  for (let i = 0; i < firstDay; i++) {
    calendarGrid.innerHTML += `
      <div class="bg-white/50 h-28 rounded-lg border border-cyan-200/30"></div>
    `;
  }

  // Group posts by date
  const postsByDate = {};
  posts.forEach(post => {
    const dateKey = new Date(post.scheduledTime)
      .toISOString()
      .split("T")[0];
    postsByDate[dateKey] = postsByDate[dateKey] || [];
    postsByDate[dateKey].push(post);
  });

  // Render days
  for (let day = 1; day <= daysInMonth; day++) {
    const dateKey = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
    const dayPosts = postsByDate[dateKey] || [];

    calendarGrid.innerHTML += `
      <div class="bg-white/60 h-28 p-3 border border-cyan-200/50 hover:border-cyan-400/50 transition-all rounded-lg group backdrop-blur-sm">
        <div class="text-sm font-bold text-cyan-600 mb-1.5">${day}</div>

        <div class="space-y-1">
          ${
            dayPosts.slice(0, 2).map(post => `
              <div class="text-[11px] px-2 py-1 rounded-md bg-cyan-100/50 text-cyan-700 font-medium truncate border border-cyan-300/50 group-hover:bg-cyan-200/60 transition">
                ${post.platform} • ${post.content}
              </div>
            `).join("")
          }

          ${
            dayPosts.length > 2
              ? `<div class="text-[10px] text-cyan-600 font-bold">
                   +${dayPosts.length - 2} more
                 </div>`
              : ""
          }
        </div>
      </div>
    `;
  }
}


  prevMonthBtn.onclick = async () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    try {
      const response = await fetch('http://localhost:8082/api/posts/all', {
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('authToken') }
      });
      const posts = await response.json();
      renderCalendar(posts);
    } catch (error) {
      console.error("Error loading posts:", error);
      renderCalendar([]);
    }
  };

  nextMonthBtn.onclick = async () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    try {
      const response = await fetch('http://localhost:8082/api/posts/all', {
        headers: { 'Authorization': 'Bearer ' + localStorage.getItem('authToken') }
      });
      const posts = await response.json();
      renderCalendar(posts);
    } catch (error) {
      console.error("Error loading posts:", error);
      renderCalendar([]);
    }
  };

  // Initial calendar render on page load
  fetchAndRenderCalendar();

});
