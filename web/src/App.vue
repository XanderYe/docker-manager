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
    overflow: hidden;
  }
  #app {
    overflow-y: auto;
  }
  #app, body, html {
    width: 100%;
    height: 100%;
  }
  body::-webkit-scrollbar {
    width: 9px;
    height: 9px;
  }

  body::-webkit-scrollbar-track {
     border-radius: 8px;
     background-color: #ffffff;
   }

  body::-webkit-scrollbar-thumb {
     border-radius: 8px;
     background-color: #d6d6d6;
   }
</style>
