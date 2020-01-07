# food log app
### *purpose: to keep track of daily meals (breakfast, lunch, dinner) and nutrition (calories, protein, carbs, fat)*

### TODO
- [x] home page
  - [x] request read/write storage permission
  - [x] progress bar for each type of nutrient, with today's progress
    - [x] raw numbers also displayed
    - [Android Tutorial: Customize a Progress Bar](https://www.youtube.com/watch?v=NKcSC60x2j0)
  - [ ] ~~meals organized by date (most recent first; refer to money tracker app for layout)~~
    - [ ] ~~breakfast, lunch, dinner in order~~
    - [ ] ~~columns for meal type, total cals/protein/carbs/fat per meal~~
    - [ ] ~~daily total listed next to date~~
- [x] new meal entry page (launched w/ FAB)
  - [x] meal type (B/L/D)
  - [ ] ~~*additional feature: if dinner selected, create checkbox for workout day (y/n)*~~
  - [x] food items ~~('+' button for each food item)~~
    - [x] name
    - [x] cals
    - [x] protein
    - [x] carbs
    - [x] fat
  - [x] total stats for all food items
  - [x] note time/date (automatic from system)
  - [x] 'finish' button
    - [x] check that total nutrients have been calculated
    - [x] check that meal type has been entered
    - [x] create schema for data entry into file
      - [ ] ~~work with [apache poi](https://github.com/centic9/poi-on-android) for excel file IO~~
        - [Create an excel file programmatically in Android](https://medium.com/@shahadat.shaki/create-an-excel-file-programmatically-in-android-d989f00e809f)
        - [Read and Display Excel Data in RecyclerView](https://www.youtube.com/watch?v=kxdVo4RH3nE&t=76s)
