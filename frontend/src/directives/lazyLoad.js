export default {
  mounted(el, binding) {
    const imageUrl = binding.value
    const options = {
      root: null,
      rootMargin: '50px',
      threshold: 0.01
    }

    const observer = new IntersectionObserver((entries, observer) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          const img = entry.target
          img.src = imageUrl
          observer.unobserve(img)
        }
      })
    }, options)

    observer.observe(el)
  }
}
