<template>
  <Header />
  <Login />
  <Register />
  <router-view></router-view>
  <!-- <Test /> -->
</template>

<script>
import components from "./components";
export default {
  name: "App",
  components,
  methods: {},
};
</script>
