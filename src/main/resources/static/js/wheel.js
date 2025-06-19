document.addEventListener("DOMContentLoaded", function () {
  const allSections = document.querySelectorAll(".bg-holder");
  let startY = 0;
  let isMoving = false;

  // 모바일 디바이스 판별 (화면 너비 아님!)
  const isMobileDevice = /Mobi|Android|iPhone|iPad|iPod/i.test(navigator.userAgent);

  allSections.forEach((section, index) => {
    // 👉 모바일 기기가 아닐 경우에만 휠 허용
    if (!isMobileDevice) {
      section.addEventListener("wheel", function (e) {
        e.preventDefault();
        const delta = e.deltaY < 0 ? 1 : -1;
        scrollByDelta(delta, index);
      }, { passive: false });
    }

    // 👉 모바일 기기에서만 터치 이벤트 작동
    if (isMobileDevice) {
      section.addEventListener("touchstart", function (e) {
        startY = e.touches[0].clientY;
      });

      section.addEventListener("touchmove", function (e) {
        if (isMoving) return;

        const currentY = e.touches[0].clientY;
        const deltaY = currentY - startY;
        const delta = deltaY > 50 ? 1 : deltaY < -50 ? -1 : 0;

        if (delta !== 0) {
          isMoving = true;
          scrollByDelta(delta, index);
          setTimeout(() => { isMoving = false; }, 800);
        }
      });
    }
  });

  function scrollByDelta(delta, index) {
    const targetIndex = delta > 0 ? index - 1 : index + 1;
    if (targetIndex >= 0 && targetIndex < allSections.length) {
      const moveTop = allSections[targetIndex].offsetTop;
      window.scrollTo({ top: moveTop, behavior: 'smooth' });
    }
  }
});
