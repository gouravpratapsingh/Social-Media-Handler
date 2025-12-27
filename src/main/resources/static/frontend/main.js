 // TAB SWITCHING
  function switchTab(tabName, evt) {
    document.querySelectorAll('.tab-btn').forEach(btn => {
      btn.classList.remove('tab-active');
    });

    if (evt) {
      let el = evt.target;
      while (el && !el.classList.contains('tab-btn')) el = el.parentElement;
      if (el) el.classList.add('tab-active');
    }

    document.getElementById('contentArea').innerHTML = `
      <h3 class="text-xl font-semibold">No ${tabName} posts</h3>
      <p class="text-gray-500 mt-2">This section is currently empty.</p>
    `;
  }

  // VIEW TOGGLE
  const listBtn = document.getElementById('listView');
  const calBtn = document.getElementById('calendarView');

  listBtn.onclick = () => {
    listBtn.classList.add('bg-blue-600', 'text-white');
    calBtn.classList.remove('bg-blue-600', 'text-white');
    calBtn.classList.add('bg-gray-100');
    alert('List View Activated');
  };

  calBtn.onclick = () => {
    calBtn.classList.add('bg-blue-600', 'text-white');
    listBtn.classList.remove('bg-blue-600', 'text-white');
    listBtn.classList.add('bg-gray-100');
    alert('Calendar View Activated');
  };

 
  const navItems = document.querySelectorAll(".nav-item");

  function setActive(item) {
    navItems.forEach(el => {
      el.classList.remove(
        "text-blue-600",
        "border-b-2",
        "border-blue-600"
      );
      el.classList.add("text-gray-500");
    });

    item.classList.add(
      "text-blue-600",
      "border-b-2",
      "border-blue-600"
    );
    item.classList.remove("text-gray-500");
  }

  // Click handling
  navItems.forEach(item => {
    item.addEventListener("click", () => {
      setActive(item);
    });
  });

  // Page-based auto highlight (IMPORTANT)
  const currentPage = window.location.pathname;

  navItems.forEach(item => {
    if (
      item.dataset.page === "publish" && currentPage.includes("index") ||
      item.dataset.page === "statistics" && currentPage.includes("statistics")
    ) {
      setActive(item);
    }
  });
  

