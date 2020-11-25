<template>

</template>

<script>
  export default {
    name: "Index",
    data() {
      return {

      }
    },
    methods: {

    },
    created() {
      this.$requests.get("/container/getContainerStatusList", {});
    }
  }
</script>

<style scoped>

</style>
