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
              "/img/Service1.png"
          },
          {
            img: true,
            src:
              "/img/Service2.png"
          },
          {
            img: true,
            src:
              "/img/Service3.png"
          },
          {
            img: true,
            src:
              "/img/Service4.png"
          },
          {
            img: true,
            src:
              "/img/Service5.png"
          },
        ]
      };
    }
  };
</script>