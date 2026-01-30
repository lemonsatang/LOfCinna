// 현재 페이지 경로 가져오기
const currentPath = window.location.pathname.split("/").pop(); // 예: 'class.html'


// 모든 메뉴 링크 가져오기
const menuLinks = document.querySelectorAll("nav a");

menuLinks.forEach(link => {
    const linkPath = link.getAttribute("href");

    if (linkPath === "/main/"+currentPath) {
        link.classList.add("active");
    }
});