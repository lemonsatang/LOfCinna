// 검색
const searchInput = document.getElementById("search-input");
const searchBtn = document.getElementById("search-btn");

// 리스트
const container = document.getElementById('data-container');
const boardList = document.querySelector(".board-list");

const size = 10;
let currentPage = 1;
let totalCount = 0;
let currentCategory = 'all';
const paginationContainer = document.querySelector('.pagination-container');

document.addEventListener("DOMContentLoaded", function(){

    getList();

})

function getList(category = currentCategory, page = 1){
    boardList.innerHTML = '';

    let keyword = searchInput.value;
    let offset = (page - 1) * size;
    currentCategory = category;
    currentPage = page;  // 현재 페이지 업데이트

    let obj = {
        keyword : keyword,
        category: category,
        size : size,
        offset : offset
    }

    $.ajax({
        url: '/board/getList',
        method: 'post',
        data: obj,
        success(res){
            if(res.length > 0){
                totalCount = res[0].totalCount;
                setList(res);
            }else{
                container.innerHTML = '<p>검색 결과가 없습니다.</p>';
                paginationContainer.innerHTML = '';
            }
        },
        error(e){
            alert("오류가 발생했습니다.")
        }
    })
}

function setList(items){
    let nowCate = document.querySelector('.active').dataset.category;

    items.forEach(item => {
        let cate = item.category;
        if(cate == 'guide'){
            cate = '공략';
        }else if(cate == 'info'){
            cate = '정보';
        }else if(cate == 'free'){
            cate = '자유';
        }

        const boardItem = document.createElement("div");
        boardItem.classList.add("board-item");
        boardItem.innerHTML = `<div class="board-item-number">${item.rownum}</div>`
        if(nowCate == 'all'){
            boardItem.innerHTML += `<div class="board-item-title"><span data-category="${item.category}">[${cate}] </span>${item.title}</div>`
        }else{
            boardItem.innerHTML += `<div class="board-item-title">${item.title}</div>`
        }
        boardItem.innerHTML += `<div class="board-item-info">${item.cmtCnt} 댓글 | ${item.cdate}</div>`;
        boardItem.addEventListener("click", () => {
            location.href = "/board/detail/"+item.bno;
        });
        boardList.appendChild(boardItem);
    });

    // 페이징
    renderPagination();
}

// ✅ 페이징 버튼 렌더링
function renderPagination() {
    paginationContainer.innerHTML = '';

    const totalPages = Math.ceil(totalCount / size);
    const maxButtons = 5; // 한 번에 보여줄 버튼 수
    const startPage = Math.floor((currentPage - 1) / maxButtons) * maxButtons + 1;
    const endPage = Math.min(startPage + maxButtons - 1, totalPages);

    for (let i = startPage; i <= endPage; i++) {
        const btn = document.createElement('button');
        btn.classList.add('page-btn');
        btn.textContent = i;
        btn.dataset.page = i;

        if (i === currentPage) {
            btn.classList.add('active');
            btn.style.background = 'navy';
            btn.style.color = 'white';
        }

        btn.addEventListener('click', () => {
            getList(currentCategory, i);
        });

        paginationContainer.appendChild(btn);
    }
}

const categoryButtons = document.querySelectorAll(".category");

// 카테고리 필터링
categoryButtons.forEach(button => {
    button.addEventListener("click", () => {
        categoryButtons.forEach(btn => btn.classList.remove("active"));
        button.classList.add("active");

        const category = button.dataset.category;
        getList(category, 1);
    });
});

// 검색 기능
searchBtn.addEventListener("click", () => {
    getList(currentCategory, 1)
});

