<template>
  <v-bottom-navigation grow absolute
                       v-model="bottomNav">
    <v-btn height="100%" value="home" :to="{ name: 'home' }">
      <span>Home</span>
      <v-icon>mdi-home</v-icon>
    </v-btn>
    <v-btn height="100%" value="poll" :to="{ name: 'poll' }">
      <span>Poll</span>
      <v-icon>mdi-poll</v-icon>
    </v-btn>
    <v-btn height="100%" value="profile" :to="{ name: 'profile' }">
      <span>Profile</span>
      <v-icon>mdi-account-circle</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: "HelloWorld",
  data: () => ({
    bottomNav: 'home',
  })
};
</script>
