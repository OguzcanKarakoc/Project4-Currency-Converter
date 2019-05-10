# Project 4: Currency Calculator

## Introduction

This project was a school project. It is an Currency Convert application build with java for android. It uses a 3rd party public api. It also shows you how much some of the popular cryptocurrencies are worth

## Showcase

(click the image to start the video, **Warning: Video has music**.)  
Please note that in th showcase you will see a tiny bug in the graph. This is because we accidently added 2 Datapoints to the same index:
```
new DataPoint(3, Double.parseDouble(result.get(3))),
new DataPoint(4, Double.parseDouble(result.get(4))),
new DataPoint(4, Double.parseDouble(result.get(5))), <--- This has been changed to 5
```

[![Watch the video](https://oguzcankarakoc.github.io/storage/project4-currency-converter/currency-converter.jpg)](https://oguzcankarakoc.github.io/storage/project4-currency-converter/showcase.mp4)

## Installation

(Minimum requirement: Java sdk version 23)
1. Clone or download the project
2. Open android studio.
3. Import all packages
4. Run the program on an Android Emulator
