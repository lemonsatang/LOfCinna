const stories = [
    {
        title: "시나클라우드(Sinacloud)",
        desc: "푸른 하늘 너머, 구름 위에 떠 있는 왕국 ‘시나클라우드(Sinacloud)’<br>이곳은 순수한 꿈과 희망의 에너지가 흐르는 천상의 대지였다.",
        img: "/image/story1.jpg"
    },
    {
        title: "그림자 폭풍",
        desc: "하지만 어느 날, 하늘의 균열이 열리며 ‘그림자 폭풍(Shadow Gale)’이 왕국을 뒤덮었다.",
        img: "/image/story2.jpg"
    },
    {
        title: "깨어난 수호자",
        desc: "희망의 힘을 먹고 자라는 어둠은 모든 구름을 검게 물들였고,<br>시나클라우드의 수호자였던 시나모롤은 네 갈래로 나뉘어 각각의 힘을 얻게 된다.",
        img: "/image/story3.jpg"
    },
    {
        title: "빛의 결정",
        desc: "이제 네 시나모롤은 각자의 길에서 **잃어버린 ‘빛의 결정(Lumina Crystal)’**을 찾아<br>하늘의 균열을 봉인하고 다시 평화를 되찾기 위한 여정을 떠난다.",
        img: "/image/story4.jpg"
    }
];

const timelineItems = document.querySelectorAll(".timeline-item");
const storyTitle = document.getElementById("story-title");
const storyDesc = document.getElementById("story-desc");
const storyVisual = document.getElementById("story-visual");

timelineItems.forEach(item => {
    item.addEventListener("click", () => {
        timelineItems.forEach(el => el.classList.remove("active"));
        item.classList.add("active");

        const index = item.dataset.index;
        const story = stories[index];

        storyTitle.textContent = story.title;
        /*storyDesc.textContent = story.desc;*/
        storyDesc.innerHTML = stories[index].desc;
        storyVisual.style.backgroundImage = "url('/image/story"+index+".png')";
    });
});