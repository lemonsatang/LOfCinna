// ✅ 아이디 찾기 팝업 열기 / 닫기
const findIdBtn = document.getElementById("findIdBtn");
const modal = document.getElementById("findIdModal");
const closeModal = document.getElementById("closeModal");

findIdBtn.addEventListener("click", () => {
    modal.style.display = "flex";
});

closeModal.addEventListener("click", () => {
    modal.style.display = "none";
    document.getElementById("findName").value = '';
    document.getElementById("findEmail").value = '';
});

// ✅ 아이디 찾기 확인 버튼 클릭 시 예시 처리
const findIdSubmit = document.getElementById("findIdSubmit");
findIdSubmit.addEventListener("click", () => {
    const name = document.getElementById("findName").value.trim();
    const email = document.getElementById("findEmail").value.trim();

    if (name === "" || email === "") {
        alert("이름과 이메일을 모두 입력해주세요.");
        return;
    }

    let obj = {
        'name' : name,
        'email' : email
    }

    $.ajax({
        url:'/main/findId',
        method:'post',
        data: obj,
        success(res) {
            if(res.state == 'exist'){
                alert("입력하신 정보로 등록된 아이디는 "+res.id+" 입니다.");
                modal.style.display = "none";
                document.getElementById("findName").value = '';
                document.getElementById("findEmail").value = '';
            }else{
                alert("입력하신 정보로 등록된 아이디는 없습니다.");
            }
        },
        error(e){
            alert("오류가 발생했습니다.");
        }
    })

    
    
});