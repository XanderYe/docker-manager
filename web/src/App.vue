<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
  body {
    margin: 0;
    padding: 0;
  }
  #app, body, html {
    width: 100%;
    height: 100%;
  }
</style>
