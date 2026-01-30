const resBtn = document.querySelector(".reserve-btn");

resBtn.addEventListener('click',function() {
    let name = document.getElementById("name").value.trim();
    let phone = document.getElementById("phone").value.trim();
    let agree = document.getElementById("agree").checked;

    if (!name || !phone) {
        alert("이름과 전화번호를 모두 입력해주세요.");
        return;
    }

    if (!agree) {
        alert("약관에 동의해야 신청이 가능합니다.");
        return;
    }

    let obj = {
        name: name,
        phone: phone
    }

    $.ajax({
        url: '/main/addPreRev',
        method: 'post',
        data: obj,
        success(res){
            alert(res.message+`🎉`);
            document.getElementById("name").value ='';
            document.getElementById("phone").value = '';
            document.getElementById("agree").checked = false;
        },
        error(e){
            alert("오류가 발생했습니다.");
        }
    })
});