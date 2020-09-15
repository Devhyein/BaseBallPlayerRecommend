<template>
  <mdb-carousel
    :items="items"
    :interval="2000"
    controlls
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
              "https://mdbootstrap.com/img/Photos/Slides/img%20(15).jpg"
          },
          {
            img: true,
            src:
              "https://mdbootstrap.com/img/Photos/Slides/img%20(16).jpg"
          },
          {
            img: true,
            src:
              "https://mdbootstrap.com/img/Photos/Slides/img%20(17).jpg"
          }
        ]
      };
    }
  };
</script>