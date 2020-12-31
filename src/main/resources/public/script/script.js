const tabcontent = document.getElementsByClassName("tabcontent");
const tablinks = document.getElementsByClassName("nav-link");
const items = document.getElementsByClassName("slider-textcard");
const itemCount = items.length;
const nextItem = document.querySelector('.next');
const previousItem = document.querySelector('.previous');
let count = 0;
const menu = document.querySelector(".menu");
const menuBlock = document.querySelector(".menu-block");
const menuItems = document.getElementsByClassName("menu-btn");
const cancel = document.querySelector(".close-btn");


function openSearch(evt,element) {
  for(let item of tabcontent) {
    item.style.display = "none";
  }

  for(let item of tablinks) {
    item.className = item.className.replace(" active","");
  }

  document.getElementById(element).style.display = "block";
  evt.currentTarget.className += " active";
}

function openPassenger(evt,element) {
  const tabpassenger = document.getElementsByClassName("tabpassenger");
  const tablinks = document.getElementsByClassName("pass-type-choice");
  for(let item of tabpassenger) {
    item.style.display = "none";
  }

  for(let item of tablinks) {
    item.className = item.className.replace(" active","");
  }

  document.getElementById(element).style.display = "block";
  evt.currentTarget.className += " active";
  return false;
}

function showNextItem() {
  items[count].classList.remove('active');

  if(count < itemCount - 1) {
    count++;
  } else {
    count = 0;
  }

  items[count].classList.add('active');
  console.log(count);
}

function showPreviousItem() {
  items[count].classList.remove('active');

  if(count > 0) {
    count--;
  } else {
    count = itemCount - 1;
  }

  items[count].classList.add('active');
  console.log(count);
}

function keyPress(e) {
  e = e || window.event;

  if (e.keyCode == '37') {
    showPreviousItem();
  } else if (e.keyCode == '39') {
    showNextItem();
  }
}

function menuPress() {
  for(let menuitem of menuItems) {
    if(menuitem.classList.contains('active')) {
      menuitem.classList.remove('active');
    } else {
      menuitem.classList.add('active');
    }
  }
  if(menuBlock.classList.contains('active')) {
    menuBlock.classList.remove('active');
  } else {
    menuBlock.classList.add('active')
  }
}

let slideIndex1 = 1;

function plusSlides(n) {
  showSlides1(slideIndex1 += n);
}

function currentSlide(n) {
  showSlides1(slideIndex1 = n);
}

function showSlides1(n) {
  let i;
  let slides = document.getElementsByClassName("Slides");
  let dots = document.getElementsByClassName("demo");
  if (n > slides.length) {slideIndex1 = 1}
  if (n < 1) {slideIndex1 = slides.length}
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex1-1].style.display = "block";
  dots[slideIndex1-1].className += " active";
}

menu.addEventListener('click',menuPress);
cancel.addEventListener('click',menuPress);
document.getElementById("default").click();
document.getElementById("default2").click();
nextItem.addEventListener('click', showNextItem);
previousItem.addEventListener('click', showPreviousItem);
document.addEventListener('keydown', keyPress);



