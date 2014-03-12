var SLIDE_CONFIG = {
  // Slide settings
  settings: {
    title: 'Rapid Reactive WebApp Development with Play on Eclipse',
    subtitle: '',
    eventInfo: {
      title: 'EclipseCon 2014',
      date: '2014-03-20'
    },
    useBuilds: true, // Default: true. False will turn off slide animation builds.
    usePrettify: true, // Default: true
    enableSlideAreas: true, // Default: true. False turns off the click areas on either slide of the slides.
    enableTouch: true, // Default: true. If touch support should enabled. Note: the device must support touch.
    //analytics: 'UA-XXXXXXXX-1', // TODO: Using this breaks GA for some reason (probably requirejs). Update your tracking code in template.html instead.
    favIcon: 'images/scala-ide-favicon.png',
    fonts: [
      'Open Sans:regular,semibold,italic,italicsemibold',
      'Source Code Pro'
    ],
    theme: ['scala-ide'], // Add your own custom themes or styles in /theme/css. Leave off the .css extension.
  },

  // Author information
  presenters: [{
    name: 'Luc Bourlier',
    company: 'Senior Software Engineer<br>Typesafe',
    gplus: 'google.com/+ScalaIDE',
    twitter: '@ScalaIDE',
    www: 'http://scala-ide.org',
    github: 'http://github.com/scala-ide'
  }/*, {
    name: 'Second Name',
    company: 'Job Title, Google',
    gplus: 'http://plus.google.com/1234567890',
    twitter: '@yourhandle',
    www: 'http://www.you.com',
    github: 'http://github.com/you'
  }*/]
};

