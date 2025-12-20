const slidesData = [
    {
        id: 1,
        city: "Rostov-on-Don<br>LCD admiral",
        apartmentArea: "81 m2",
        repairTime: "3.5 months",
        repairCost: "Upon request",
        title: "ROSTOV-ON-DON, ADMIRAL",
        imageUrl: "img/Rostov-on-Don_Admiral.png",
        linkText: "ROSTOV-ON-DON, ADMIRAL"
    },
    {
        id: 2,
        city: "Sochi<br>Thieves",
        apartmentArea: "105 m2",
        repairTime: "4 months",
        repairCost: "Upon request",
        title: "SOCHI THIEVES",
        imageUrl: "img/Sochi_Thieves.png",
        linkText: "SOCHI THIEVES"
    },
    {
        id: 3,
        city: "Rostov-on-Don<br>Patriotic",
        apartmentArea: "93 m2",
        repairTime: "3 months",
        repairCost: "Upon request",
        title: "ROSTOV-ON-DON PATRIOTIC",
        imageUrl: "img/Rostov-on-Don_Patriotic.png",
        linkText: "ROSTOV-ON-DON PATRIOTIC"
    }
];

const slider = document.getElementById('slider');
const dotsContainer = document.getElementById('dots');
const linksContainer = document.getElementById('project-links');
const currentSlideElement = document.getElementById('current-slide');
const totalSlidesElement = document.getElementById('total-slides');
const prevArrow = document.querySelector('.arrow-prev');
const nextArrow = document.querySelector('.arrow-next');

let currentSlideIndex = 0;
const totalSlides = slidesData.length;

function initSlider() {
    totalSlidesElement.textContent = totalSlides.toString().padStart(2, '0');
    
    slidesData.forEach((slide, index) => {
        const slideElement = document.createElement('div');
        slideElement.className = `slide ${index === 0 ? 'active' : ''}`;
        slideElement.setAttribute('data-slide-index', index);
        
        slideElement.innerHTML = `
            <div class="slide-info">
                <div class="info-header">
                    <h2>COMPLITED</h2>
                    <h2>PROJECTS   _______</h2>
                </div>
                
                    <div class="info-hvalue"> Only a small part of the work performed by our company is presented on the site. For 14 years on in the construction market we have made happy more than 1000 families
                    </div>
                <div class="info-line">
                    <div class="info-item">
                        <div class="info-label">CITY:</div>
                        <div class="info-value">${slide.city}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">APPARTMENT AREA:</div>
                        <div class="info-value">${slide.apartmentArea}</div>
                    </div>
                </div>
                <div class="info-line">
                    <div class="info-item">
                        <div class="info-label">REPAIR TIME:</div>
                        <div class="info-value">${slide.repairTime}</div>
                    </div>
                    <div class="info-item">
                        <div class="info-label">REPAIR COST:</div>
                        <div class="info-value">${slide.repairCost}</div>
                    </div>
                </div>

            </div>
            </div>
            <div class="slide-image">
                <img src="${slide.imageUrl}" alt="${slide.title}">
            </div>
            
        `;
        
        slider.appendChild(slideElement);
        
        const dotElement = document.createElement('div');
        dotElement.className = `dot ${index === 0 ? 'active' : ''}`;
        dotElement.setAttribute('data-slide-index', index);
        dotsContainer.appendChild(dotElement);
        
        const linkElement = document.createElement('div');
        linkElement.className = `project-link ${index === 0 ? 'active' : ''}`;
        linkElement.setAttribute('data-slide-index', index);
        linkElement.innerHTML = slide.linkText;
        linksContainer.appendChild(linkElement);
    });
    
    document.querySelectorAll('.dot').forEach(dot => {
        dot.addEventListener('click', () => {
            const slideIndex = parseInt(dot.getAttribute('data-slide-index'));
            goToSlide(slideIndex);
        });
    });
    
    document.querySelectorAll('.project-link').forEach(link => {
        link.addEventListener('click', () => {
            const slideIndex = parseInt(link.getAttribute('data-slide-index'));
            goToSlide(slideIndex);
        });
    });
    
    prevArrow.addEventListener('click', () => {
        goToSlide(currentSlideIndex - 1);
    });
    
    nextArrow.addEventListener('click', () => {
        goToSlide(currentSlideIndex + 1);
    });
    
    updateCurrentSlideDisplay();
}

function goToSlide(slideIndex) {
    if (slideIndex < 0) {
        slideIndex = totalSlides - 1;
    } else if (slideIndex >= totalSlides) {
        slideIndex = 0;
    }
    
    currentSlideIndex = slideIndex;
    
    document.querySelectorAll('.slide').forEach((slide, index) => {
        slide.classList.remove('active');
        if (index === slideIndex) {
            slide.classList.add('active');
        }
    });
    
    document.querySelectorAll('.dot').forEach((dot, index) => {
        dot.classList.remove('active');
        if (index === slideIndex) {
            dot.classList.add('active');
        }
    });
    
    document.querySelectorAll('.project-link').forEach((link, index) => {
        link.classList.remove('active');
        if (index === slideIndex) {
            link.classList.add('active');
        }
    });
    
    updateCurrentSlideDisplay();
}

function updateCurrentSlideDisplay() {
    currentSlideElement.textContent = (currentSlideIndex + 1).toString().padStart(2, '0');
}

document.addEventListener('DOMContentLoaded', initSlider);

document.addEventListener('keydown', (e) => {
    if (e.key === 'ArrowLeft') {
        goToSlide(currentSlideIndex - 1);
    } else if (e.key === 'ArrowRight') {
        goToSlide(currentSlideIndex + 1);
    }
});