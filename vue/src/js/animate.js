// 动画函数
function animateFun(obj, target, time = 15) {
    clearInterval(obj.timer);
    obj.timer = setInterval(function () {
        let step = (target - obj.offsetLeft) / 10;
        step = step > 10 ? Math.ceil(step) : Math.floor(step);
        if (obj.offsetLeft === target) {
            clearInterval(obj.timer)
        }
        obj.style.left = obj.offsetLeft + step + 'px'
    }, time)
}
export default {
    animateFun
}