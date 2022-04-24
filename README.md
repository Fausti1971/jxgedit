# JXG
Java XG-Editor

Simple small Editor for XG-Devices close to Yamaha-XG-Specification 2.00 of April 2001.
By default the specs are readed by the following files in the ".jar/xml/"-directory:
defaults.xml = defaultvalues,
drums.xml = drumnames,
parameter.xml = parameterspecification,
device.xml = devicestructure,
tables.xml = tables for valuetranslation and
template.xml = configuration for editorwindows(future).
You can customize your device by copy (one or more of) these files in a folder (named as your device) beside the ".jar"-file,
edit it for device preferences, type (or detect) devicename in settingswindow and restart. Now the specs will be overridden by the available specs of the ".xml"-file in your device-folder.
If you have got more than one XG-device, you can rename the jar-file as you like and the config-file will be named by the name of the jar-file with extension ".xml" and saved beside the .jar.