<template>
  <mdb-carousel
    :items="items"
    :interval="6000"
    slide
    controlls
    indicators
  ></mdb-carousel>
</template>
<script>
  import { mdbCarousel } from "mdbvue";
  export default {
    name: "CarouselPage",
    components: {
      mdbCarousel
    },
    data() {
      return {
        items: [
          {
            img: true,
            src:
              "/img/news1.jpg"
          },
          {
            img: true,
            src:
              "/img/news2.jpg"
          },
          {
            img: true,
            src:
              "/img/news3.jpg"
          }
        ]
      };
    }
  };
</script>