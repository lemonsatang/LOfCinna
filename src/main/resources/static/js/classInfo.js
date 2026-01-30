const classData = [
    {
        name: "전사",
        desc: "플레이 난이도: ⭐<br><br>용감한 하늘의 수호자. 거대한 구름검을 들고 전장을 누비며<br>어둠의 군세로부터 친구들을 지킨다.",
        img: "/image/class_warrior.png"
    },
    {
        name: "마법사",
        desc: "플레이 난이도: ⭐⭐⭐<br><br>별빛의 힘을 다루는 천재 마법사.<br>하늘의 마력으로 시간과 공간을 조작하며,<br>무너진 왕국의 질서를 회복하기 위한 고대 주문을 연구한다.",
        img: "/image/class_mage.png"
    },
    {
        name: "궁수",
        desc: "플레이 난이도: ⭐⭐⭐<br><br>구름 숲의 정찰자. 하늘의 바람을 읽어,<br>적의 움직임을 예측하고 번개 같은 화살로 상대를 제압한다.",
        img: "/image/class_archer.png"
    },
    {
        name: "도적",
        desc: "플레이 난이도: ⭐⭐⭐⭐⭐<br><br>어둠 속에서도 빛을 잃지 않는 재치꾼.<br>날렵한 움직임과 교활한 전략으로 적진을 교란하고<br>숨겨진 보물을 찾아 왕국의 비밀을 파헤친다.",
        img: "/image/class_rogue.png"
    }
];

const icons = document.querySelectorAll(".class-icon");
const nameEl = document.getElementById("class-name");
const descEl = document.getElementById("class-desc");
const imgEl = document.getElementById("class-img");

icons.forEach((icon, index) => {
    icon.addEventListener("click", () => {
        icons.forEach(i => i.classList.remove("active"));
        icon.classList.add("active");

        const data = classData[index];
        nameEl.textContent = data.name;
        descEl.innerHTML = data.desc;
        imgEl.src = data.img;
    });
});