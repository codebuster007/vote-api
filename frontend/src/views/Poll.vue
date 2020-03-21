<template>
  <v-container fluid>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title class="wrap">Who is the Founder of Microsoft?</v-card-title>
          <v-card-text>
            <v-progress-linear
              class="white--text"
              rounded
              v-model="power"
              color="primary"
              height="25"
              reactive
            >
              <strong>Jeff Bezos</strong>
            </v-progress-linear>
            <v-spacer class="ma-3"> </v-spacer>
            <v-progress-linear
              class="white--text"
              rounded
              v-model="skill"
              color="primary"
              height="25"
              reactive
            >
              <strong>Bill Gates</strong>
            </v-progress-linear>

            <v-spacer class="ma-3"> </v-spacer>

            <v-progress-linear
              class="white--text"
              rounded
              v-model="knowledge"
              color="primary"
              height="25"
              reactive
            >
              <strong>Elon Musk</strong>
            </v-progress-linear>
          </v-card-text>
          <v-card-actions class="align-content-end">
            <v-btn class="elevation-0 white" style="border-width: 4px"
              >End Poll</v-btn>
            <v-btn class="elevation-0 white">Extend Time</v-btn>
          </v-card-actions>
        </v-card>

        <v-card-text style="height: 100px; position: relative">
          <v-btn
            class="align-self-baseline"
            absolute
            dark
            fab
            bottom
            right
            color="primary"
            ><v-icon>mdi-plus</v-icon>
          </v-btn>
        </v-card-text>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "Poll",
  data: () => ({
    skill: 20,
    knowledge: 33,
    power: 78
  })
};
</script>
