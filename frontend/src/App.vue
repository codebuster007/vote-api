<template>
  <v-app id="app">
    <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
          transition="scale-transition"
          width="40"
        />
        <span
          class="headline white--text shrink mt-1 hidden-sm-and-down"
          >Voter App</span>
      </div>

      <v-spacer></v-spacer>

      <v-row justify="end">
        <v-dialog v-model="dialog" persistent max-width="600px">
          <template v-slot:activator="{ on }">
            <v-btn class="v-btn--flat elevation-0" color="primary" v-on="on">
              <span class="mr-2">Sign Up</span>
              <v-icon>mdi-login</v-icon>
            </v-btn>
          </template>

          <SignUp :dialog="dialog" @close-dialog="closeDialog" />
        </v-dialog>
      </v-row>
    </v-app-bar>

    <v-content class="mt-5">
      <HelloWorld />
      <router-view />
    </v-content>
  </v-app>
</template>

<script>
import HelloWorld from "./components/HelloWorld";
import SignUp from "./components/Signup";

export default {
  name: "App",

  components: {
    SignUp,
    HelloWorld
  },

  data: () => ({
    //
    dialog: false
  }),

  methods: {
    closeDialog() {
      this.dialog = true;
      this.dialog = false;
    }
  }
};
</script>
