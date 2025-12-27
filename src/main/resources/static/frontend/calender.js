// Select Buttons
const listViewBtn = document.getElementById("listViewBtn");
const calendarViewBtn = document.getElementById("calendarViewBtn");

// Select Content Sections
const listViewSection = document.getElementById("listViewContent");
const calendarViewSection = document.getElementById("calendarViewContent");

// Calendar Elements
const calendarTitle = document.getElementById("calendarTitle");
const calendarGrid = document.getElementById("calendarGrid");
const prevMonthBtn = document.getElementById("prevMonth");
const nextMonthBtn = document.getElementById("nextMonth");

// ----------------------------
// VIEW TOGGLE LOGIC
// ----------------------------
listViewBtn.onclick = () => {
  // Show List, Hide Calendar
  listViewSection.classList.remove("hidden");
  calendarViewSection.classList.add("hidden");

  // Update Button Styles
  listViewBtn.classList.add("bg-white", "text-blue-600", "shadow");
  listViewBtn.classList.remove("text-gray-600");
  calendarViewBtn.classList.remove("bg-white", "text-blue-600", "shadow");
  calendarViewBtn.classList.add("text-gray-600");
};

calendarViewBtn.onclick = () => {
  // Show Calendar, Hide List
  calendarViewSection.classList.remove("hidden");
  listViewSection.classList.add("hidden");

  // Update Button Styles
  calendarViewBtn.classList.add("bg-white", "text-blue-600", "shadow");
  calendarViewBtn.classList.remove("text-gray-600");
  listViewBtn.classList.remove("bg-white", "text-blue-600", "shadow");
  listViewBtn.classList.add("text-gray-600");

  renderCalendar();
};

// ----------------------------
// MOCK DATA & CALENDAR RENDER
// ----------------------------
const scheduledPosts = {
  "2025-01-05": 2,
  "2025-01-10": 1,
  "2025-01-18": 3
};

let currentDate = new Date();

function renderCalendar() {
  calendarGrid.innerHTML = "";
  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  calendarTitle.textContent = currentDate.toLocaleString("default", { month: "long" }) + " " + year;

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  // Add empty cells for days from previous month
  for (let i = 0; i < firstDay; i++) {
    calendarGrid.innerHTML += `<div class="bg-gray-50 h-24 border-r border-b border-gray-100"></div>`;
  }

  // Add actual days
  for (let day = 1; day <= daysInMonth; day++) {
    const dateKey = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
    const count = scheduledPosts[dateKey];

    calendarGrid.innerHTML += `
      <div class="bg-white h-24 p-2 border-r border-b border-gray-100 hover:bg-blue-50 transition cursor-pointer">
        <div class="font-semibold text-gray-700">${day}</div>
        ${
          count
            ? `<div class="mt-2 px-2 py-1 bg-blue-100 text-blue-700 text-[10px] font-bold rounded-md">${count} POSTS</div>`
            : `<div class="mt-2 text-[10px] text-gray-300 uppercase font-medium">No posts</div>`
        }
      </div>
    `;
  }
}

// Month Navigation
prevMonthBtn.onclick = () => {
  currentDate.setMonth(currentDate.getMonth() - 1);
  renderCalendar();
};

nextMonthBtn.onclick = () => {
  currentDate.setMonth(currentDate.getMonth() + 1);
  renderCalendar();
};