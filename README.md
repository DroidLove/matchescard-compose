# matches-card-compose

A 2017 Android demo rewritten in Compose. **The interesting artifact is the diff, not the app.**

The original ([`DroidLove/MatchesCard`](https://github.com/DroidLove/MatchesCard)) is my own code:
`android.support.*`, MVP presenter, RxJava2 + OkHttp, `RecyclerView` with a layout animation, a
`ShapeImageView` for avatars. It worked. It is also a museum of decisions I would not make now.

Translating XML into Compose isn't the point — an agent can do that in an afternoon. Knowing *which*
decisions to change, and being able to say why in a review, is the point. Everything below is one
of those decisions, and most of them are visible in the tests.

---

## What changed, and why

| Concern | 2018 | Now | Why |
|---|---|---|---|
| Layout | `CardView` + `ConstraintLayout` item XML | stateless `MatchCard` composable | one file, no `findViewById`, no binding class |
| State | MVP presenter → view, mutable list inside the adapter | one `MatchesUiState` + pure `reduce()` | the logic is now testable without Android |
| Row removal | `notifyItemRemoved(position)` | remove **by id** | a double tap or a stale callback removes the wrong person when positions shift |
| Screen phases | `progressBar` visible + `linearLayoutContent` visible | one exhaustive `Phase` enum | the old flags allowed "empty state visible while loading", which is a real bug |
| Names | `.capitalize()` on every field | render what the server sent | `.capitalize()` turns **D'Souza** into *D'souza* |
| Date of birth | hardcoded `18 Mar 1993` for every profile | per-profile, fixed pattern | a placeholder that shipped |
| Call / email | `startActivity` unguarded | `ActivityNotFoundException` caught | no dialer is normal on tablets and emulators |
| Touch targets | 38dp buttons | 48dp minimum | 38dp is below the accessibility floor |
| Screen readers | no content descriptions | `Call <name>`, `Email <name>`, text labels | TalkBack announced "Button" four times per card |
| Colour | white on `#FF5A60` = **3.05:1** | white on `#C2182B` = **6.07:1** | the original failed WCAG AA for normal text |
| Avatars | remote JPEG + gender-specific placeholder | initials on a tinted circle | network, cache and wrong-placeholder failures, for a decoration |
| Sizing | 27 `dimen_*` values | Material3 theme + shapes | `dimen_18` communicates nothing |

Contrast ratios above were computed from the palette with the WCAG 2.1 relative-luminance formula,
not eyeballed.

---

## Measured

Apple M2, 16 GB, macOS 26.6.2, JDK 17 — all on the JVM, no emulator, no device.

| | |
|---|---|
| Tests | **14** — 8 reducer, 3 interaction, 3 screenshot |
| `:app:testDebugUnitTest` (warm) | **~5s** |
| First build from scratch (downloads included) | **39s** |
| Golden size | 822×1782 (411×891dp @ xhdpi) |

The 3 screenshot tests render the real Compose tree with Robolectric's native graphics and compare
against committed PNGs, so a layout regression fails a test instead of needing a human to open an
emulator.

---

## Decisions I would defend in review

**The reducer is a pure function.** `reduce(state, action): state` has no Android types, no
coroutines, no clock. The eight tests covering it run in milliseconds. The alternative — asserting
through a `RecyclerView` adapter — is where the original's bugs lived.

**Remove by id, never by index.** `notifyItemRemoved(position)` is correct only if nothing else
changed in between. If two taps land before the next frame, the second removes the wrong profile.
An unknown id is a no-op, so a stale callback cannot destroy someone else's card. There is a test
for exactly this.

**Four states, not two booleans.** `Loading`, `Content`, `Empty`, `Error`. Two visibility flags can
represent four combinations, and one of them is nonsense. An enum makes the nonsense unrepresentable.

**Rendering is deterministic.** Fixture data, a fixed date pattern with an explicit locale, no
network, no random, no `LocalDate.now()`. A golden that depends on the machine is a test that
teaches people to re-record instead of read.

**Accessibility is not polish.** 48dp targets, labelled icon buttons, and a primary colour that
carries white text. None of these are hard; all of them are invisible in a demo and obvious to the
user who needs them.

**Keep the platform APIs boring.** No DI framework, no custom image view, no custom shape drawable.
Two screens' worth of app does not need a framework, and a framework is another thing an agent can
misuse.

---

## What I deliberately did not build

- **Image loading (Coil).** It replaces `InitialsAvatar` and nothing else. Adding it now would make
  the goldens depend on a network response.
- **Swipe to dismiss.** A destructive gesture without undo is worse than a button. The buttons are
  explicit and testable.
- **Pagination.** The reducer does not care where the list came from; adding paging is a change to
  the load effect, not to the state model.
- **MVP, kept for compatibility.** There is nothing to keep. The presenter existed to move data
  from one object to another.
- **A repository interface for one screen.** The fixture is a function of the ViewModel; a real
  implementation replaces `load()`.

---

## Run it

```bash
./gradlew :app:assembleDebug          # build
./gradlew :app:testDebugUnitTest      # 14 tests, no emulator
./gradlew :app:recordRoborazziDebug   # re-record goldens (review the PNGs first)
./gradlew :app:verifyRoborazziDebug   # fail on any pixel diff
```

Requires JDK 17 and Android SDK 35. The Gradle wrapper is committed.

---

## License

MIT.
