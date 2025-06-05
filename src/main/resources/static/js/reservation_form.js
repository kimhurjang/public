// 인원수 라디오 버튼 생성
function renderGuestOptions(capacity) {
  //console.log("[디버그] renderGuestOptions 실행됨 / capacity =", capacity);

  const guestContainer = document.getElementById('guestCntContainer');
  guestContainer.innerHTML = ''; // 기존 라디오 버튼 제거

  for (let i = 100; i <= capacity; i += 50) {
    const id = `guestCnt_${i}`;

    const input = document.createElement('input');
    input.type = 'radio';
    input.name = 'guestCnt'; // 서버로 전송될 필드명
    input.value = i;
    input.id = id;

    const label = document.createElement('label');
    label.htmlFor = id;
    label.innerText = `${i}명`;

    guestContainer.appendChild(input);
    guestContainer.appendChild(label);
  }
  document.querySelectorAll('input[name="guestCnt"]').forEach(radio => {
    radio.addEventListener("click", function (e) {
      //console.log("[디버그] guestCnt 라디오 클릭됨 / value =", this.value);
      const hallSelected = document.querySelector('input[name="hallIdx"]:checked');
      if (!hallSelected) {
        e.preventDefault();
        alert("홀을 먼저 선택하세요.");
        document.querySelector('input[name="hallIdx"]')?.focus();
      }
    });
  });
}

// 금액 산출 버튼 클릭 처리
function bindCalcPrice() {
  document.getElementById("calc_price_btn").addEventListener("click", function () {
    const hallRadio = document.querySelector('input[name="hallIdx"]:checked');
    const guestRadio = document.querySelector('input[name="guestCnt"]:checked');
    const mealRadio = document.querySelector('input[name="mealType"]:checked');
    const flowerRadio = document.querySelector('input[name="flower"]:checked');
    const displayInput = document.getElementById("price_result_display");
    const hiddenInput = document.getElementById("price_result");

    if (!hallRadio) { alert("홀을 먼저 선택하세요."); hallRadio?.focus(); return; }
    if (!guestRadio) { alert("인원수를 선택하세요."); guestRadio?.focus(); return; }
    if (!mealRadio) { alert("식사 종류를 선택하세요."); mealRadio?.focus(); return; }
    if (!flowerRadio) { alert("꽃 종류를 선택하세요."); flowerRadio?.focus(); return; }

    const hallId = parseInt(hallRadio.value);
    const hallPrice = hallMap[hallId].price;
    const mealPrice = { "뷔페A실속": 13000, "뷔페B실속": 8000, "도시락": 7000 }[mealRadio.value] || 0;
    const flowerPrice = { "생화": 7000, "조화 + 생화": 5000, "조화": 4000 }[flowerRadio.value] || 0;
    const guestCnt = parseInt(guestRadio.value);

    const total = (hallPrice + mealPrice + flowerPrice) * guestCnt;

    displayInput.textContent = total.toLocaleString() + "원"; // 사용자용 (콤마 포함)
    hiddenInput.value = total;                   // 서버용 (콤마 제거)
  });
}
//onclick 용 금액산출버튼
function calcPrice() {
    const hallRadio = document.querySelector('input[name="hallIdx"]:checked');
    const guestRadio = document.querySelector('input[name="guestCnt"]:checked');
    const mealRadio = document.querySelector('input[name="mealType"]:checked');
    const flowerRadio = document.querySelector('input[name="flower"]:checked');
    const displayInput = document.getElementById("price_result_display");
    const hiddenInput = document.getElementById("price_result");

    if (!hallRadio) { alert("홀을 먼저 선택하세요."); hallRadio?.focus(); return; }
    if (!guestRadio) { alert("인원수를 선택하세요."); guestRadio?.focus(); return; }
    if (!mealRadio) { alert("식사 종류를 선택하세요."); mealRadio?.focus(); return; }
    if (!flowerRadio) { alert("꽃 종류를 선택하세요."); flowerRadio?.focus(); return; }

    const hallId = parseInt(hallRadio.value);
    const hallPrice = hallMap[hallId].price;
    const mealPrice = { "뷔페A실속": 13000, "뷔페B실속": 8000, "도시락": 7000 }[mealRadio.value] || 0;
    const flowerPrice = { "생화": 7000, "조화 + 생화": 5000, "조화": 4000 }[flowerRadio.value] || 0;
    const guestCnt = parseInt(guestRadio.value);

    const total = (hallPrice + mealPrice + flowerPrice) * guestCnt;

    displayInput.textContent = total.toLocaleString() + "원"; // 사용자용 (콤마 포함)
    hiddenInput.value = total;                   // 서버용 (콤마 제거)
}

// 초기화 버튼 클릭 처리
function bindReset() {
  document.getElementById("reset_btn").addEventListener("click", function () {
    ["hallIdx", "guestCnt", "mealType", "flower"].forEach(name => {
      document.querySelectorAll(`input[name="${name}"]`).forEach(radio => radio.checked = false);
    });
    document.getElementById("price_result_display").textContent = "";
    document.getElementById("price_result").value = "";
    //document.getElementById("guestCntContainer").innerHTML = '';
  });
}

// 행사일 '미정' 체크 처리
function bindUndecidedToggle() {
  const eventDateInput = document.getElementById('event_date_input');
  const eventTimeSelect = document.getElementById('event_time_select');
  const eventUndecided = document.getElementById('event_date_undecided');

  eventUndecided.addEventListener('change', function () {
    const disabled = this.checked;
    eventDateInput.disabled = disabled;
    eventTimeSelect.disabled = disabled;
    if (disabled) {
      eventDateInput.value = '';
      eventTimeSelect.value = '';
    }
  });
}

// 당일포함 이전 날자 선택 안되도록 함
function bindDateValidations() {
  const dateTargets = [
    { id: "contact_date", label: "상담 가능 날짜" },
    { id: "event_date_input", label: "행사 예정일" }
  ];

  dateTargets.forEach(({ id, label }) => {
    const input = document.getElementById(id);
    if (!input) return;

    input.addEventListener("change", function () {
      const selected = new Date(this.value + "T00:00:00"); // 시간 00시로 고정
      const today = new Date();
      today.setHours(0, 0, 0, 0); // 오늘도 00시로 고정

      //console.log(`선택된 날짜: ${selected}`);
      //console.log(`오늘 날짜: ${today}`);

      if (selected <= today) {
        alert(`${label}은 당일 이후만 선택 가능합니다.`);
        this.value = "";
        this.focus();
      }

      // 상담 가능 날짜가 행사 예정일보다 최소 2주 이상 차이 나야 함
      if (id === "contact_date") {
        const eventInput = document.getElementById("event_date_input");
        if (eventInput && eventInput.value) {
          const eventDate = new Date(eventInput.value);
          const twoWeeksBeforeEvent = new Date(eventDate);
          twoWeeksBeforeEvent.setDate(eventDate.getDate() - 14);

          //console.log(`행사 예정일: ${eventDate}`);
          //console.log(`상담 가능 최소 날짜: ${twoWeeksBeforeEvent}`);

          if (selected > twoWeeksBeforeEvent) {
            alert(`상담 가능 날짜는 행사 예정일 최소 2주 이전이어야 합니다.`);
            this.value = "";
            this.focus();
          }
        }
      }
    });
  });
}

// 유효성 검사 (form submit 전에 호출됨)
function validateForm() {
  const name = document.getElementById('name_input');
  const mobile = document.getElementById('contact_tel');
  const date = document.getElementById('contact_date');
  const time = document.getElementById('contact_time_select');
  const contactTime = document.getElementById('contactTime');
  const privacy = document.getElementById('agree_privacy');

  if (!name.value.trim()) { alert("이름을 입력해주세요."); name.focus(); return false; }
  if (!mobile.value.trim()) { alert("연락처를 입력해주세요."); mobile.focus(); return false; }
  if (!date.value) { alert("상담 가능 날짜를 선택해주세요."); date.focus(); return false; }
  if (!time.value) { alert("상담 가능 시간을 선택해주세요."); time.focus(); return false; }

  contactTime.value = date.value + ' / ' + time.value;

  if (!privacy.checked) { alert("개인정보 수집 및 이용에 동의해주세요."); privacy.focus(); return false; }

  return true;
}

// DOM 로드 완료 후 이벤트 바인딩
document.addEventListener("DOMContentLoaded", function () {
  //console.log("DOMContentLoaded 실행됨!");

  renderGuestOptions(); // 인원수 기본 렌더링

  // 홀 선택 시 인원수 옵션 다시 생성
  document.querySelectorAll('input[name="hallIdx"]').forEach(radio => {
    radio.addEventListener('change', function () {
      //console.log("[디버그] hallIdx 선택됨! this.dataset =", this.dataset);
      //console.log("[디버그] this.dataset.capacity =", this.dataset.capacity);

      const capacity = parseInt(this.dataset.capacity);
      //console.log("[디버그] capacity 변환 결과 =", capacity);

      renderGuestOptions(capacity);
    });
  });

  //bindCalcPrice(); // 금액 산출 -> 이건 onclick으로
  bindReset(); // 초기화
  bindUndecidedToggle(); // 행사일 미정 체크
  bindDateValidations(); // 날짜 유효성

});