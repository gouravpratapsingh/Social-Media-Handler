 // Simple Form Interactivity
        const form = document.getElementById('hero-form');
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            const email = form.querySelector('input').value;
            alert(`Thanks! Redirecting to setup for: ${email}`);
            // Here you would typically redirect to a signup page
        });

        // Add a subtle parallax movement to floating icons via JS
        document.addEventListener('mousemove', (e) => {
            const icons = document.querySelectorAll('main > img');
            const x = (window.innerWidth - e.pageX * 2) / 100;
            const y = (window.innerHeight - e.pageY * 2) / 100;

            icons.forEach((icon, index) => {
                const speed = (index + 1) * 0.5;
                icon.style.transform = `translateX(${x * speed}px) translateY(${y * speed}px)`;
            });
        });
        const animateCounters = () => {
  const counters = document.querySelectorAll('.counter');
  const speed = 100; // Lower is faster

  counters.forEach(counter => {
    const updateCount = () => {
      const target = +counter.getAttribute('data-target');
      const count = +counter.innerText.replace(/,/g, ''); // Remove commas to calculate
      
      // Calculate increment based on target size
      const inc = Math.ceil(target / speed);

      if (count < target) {
        const nextValue = count + inc > target ? target : count + inc;
        // Format with commas for display
        counter.innerText = nextValue.toLocaleString();
        setTimeout(updateCount, 20);
      } else {
        counter.innerText = target.toLocaleString();
      }
    };
    updateCount();
  });
};

// Intersection Observer to start counting only when visible on screen
const observerOptions = {
  threshold: 0.5 // Start when 50% of the section is visible
};

const observer = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      animateCounters();
      observer.unobserve(entry.target); // Stop observing after animation starts
    }
  });
}, observerOptions);

document.querySelectorAll('.counter').forEach(c => observer.observe(c));
//for card grid
const cardData = {
    publish: {
        title: "Publishing Tools",
        body: "Schedule your content across multiple social channels. Features include automated scheduling, platform-specific customisations, and a visual calendar view.",
        color: "#FFB1A1"
    },
    create: {
        title: "Content Creation",
        body: "Turn ideas into high-performing posts. Use our 'Ideas' board to store inspiration and the AI Assistant to help refine your copy and generate creative concepts.",
        color: "#D1C4FF"
    },
    analyze: {
        title: "Advanced Analytics",
        body: "Deep-dive into performance metrics. Understand your audience demographics, track engagement rates over time, and generate professional PDF reports in one click.",
        color: "#C1E8FF"
    },
    engage: {
        title: "Audience Engagement",
        body: "Never miss a conversation. Our engagement dashboard prioritizes important comments and messages, allowing you to reply to your fans faster and build community.",
        color: "#D6F5D8"
    }
};

function openModal(type) {
  const modal = document.getElementById('infoModal');
  const content = document.getElementById('modalContent');
  const data = cardData[type];

  if (!modal || !content || !data) return;

  content.innerHTML = `
      <div class="w-16 h-2 rounded-full mb-6" style="background-color: ${data.color}"></div>
      <h2 class="text-3xl font-bold text-slate-900 mb-4">${data.title}</h2>
      <p class="text-slate-600 text-lg leading-relaxed mb-8">${data.body}</p>
      <button onclick="closeModal()" class="w-full bg-slate-900 text-white py-4 rounded-full font-bold hover:bg-slate-800 transition shadow-lg">
        Close details
      </button>
    `;
  modal.classList.remove('hidden');
  document.body.style.overflow = 'hidden';
}

function closeModal() {
  const modal = document.getElementById('infoModal');
  if (modal) modal.classList.add('hidden');
  document.body.style.overflow = 'auto';
}

// Close if user clicks outside the modal box
window.onclick = (e) => {
  const modal = document.getElementById('infoModal');
  if (modal && e.target === modal) closeModal();
};
const featureModal = document.getElementById("featureModal");
const openBtn = document.getElementById("learnMorePublish");
const closeBtn = document.getElementById("closeModal");

if (featureModal && openBtn) {
  openBtn.addEventListener("click", () => {
    featureModal.classList.remove("hidden");
    featureModal.classList.add("flex");
    document.body.style.overflow = 'hidden';
  });

  // close handlers only make sense when the modal exists
  if (closeBtn) closeBtn.addEventListener("click", closeFeatureModal);

  featureModal.addEventListener("click", e => {
    if (e.target === featureModal) closeFeatureModal();
  });
}

function closeFeatureModal() {
  if (!featureModal) return;
  featureModal.classList.add("hidden");
  featureModal.classList.remove("flex");
  document.body.style.overflow = 'auto';
}
  
  const resourceModal = document.getElementById("resourceModal");
  const closeResourceBtn = document.getElementById("closeResourceModal");
  const titleEl = document.getElementById("resourceTitle");
  const descEl = document.getElementById("resourceDescription");

  const resourceData = {
    tools: {
      title: "Free Marketing Tools",
      description:
        "Access a curated set of free tools to streamline content creation, scheduling, and optimization."
    },
    glossary: {
      title: "Social Media Glossary",
      description:
        "Understand the most common social media terms and metrics without the jargon."
    },
    marketing101: {
      title: "Social Media Marketing 101",
      description:
        "Learn the fundamentals of social media marketing—from strategy to execution."
    },
    besttime: {
      title: "Best Time to Post",
      description:
        "Discover data-backed insights on when to post to maximize reach and engagement."
    },
    resources: {
      title: "Social Media Resources",
      description:
        "Explore articles, interviews, and deep dives packed with actionable insights."
    }
  };

  function openResourceModal(key) {
    titleEl.textContent = resourceData[key].title;
    descEl.textContent = resourceData[key].description;
    resourceModal.classList.remove("hidden");
    resourceModal.classList.add("flex");
  }

  document.querySelectorAll(".resource-card").forEach(card => {
    card.addEventListener("click", () => {
      openResourceModal(card.dataset.resource);
    });
  });

  closeResourceBtn.addEventListener("click", closeResourceModal);
  resourceModal.addEventListener("click", e => {
    if (e.target === resourceModal) closeResourceModal();
  });

  function closeResourceModal() {
    resourceModal.classList.add("hidden");
    resourceModal.classList.remove("flex");
  }

   document.getElementById("ctaButton").addEventListener("click", () => {
    document.getElementById("signup").scrollIntoView({
      behavior: "smooth"
    });
  });