# Recommendation Formula — Honor of Kings IMS

> This document defines the scoring formulas used by RecommendationService
> to recommend heroes and equipment to players.

---

## 1. Hero Recommendation Score

### Formula

```
Hero Score =
  0.30 × typePreferenceScore
+ 0.25 × heroWinRateScore
+ 0.20 × teamNeedScore
+ 0.15 × playerHistoryScore
+ 0.10 × equipmentCompatibilityScore
```

Each sub-score ranges from 0 to 100. Final score ranges from 0 to 100.
Heroes are ranked by final score (descending).

### Sub-Score Definitions

#### 1.1 typePreferenceScore (weight 0.30)

Measures how well the hero's type matches the player's preferred hero types.

| Condition | Score |
|-----------|-------|
| Hero type matches player's most-used type | 100 |
| Hero type matches any hero type the player owns | 60 |
| Hero type does not match any owned hero's type | 20 |

**How it works**: Count owned heroes by type. The type with the most heroes
is the player's "most-used type". If the candidate hero is that type, score = 100.
If it matches any other owned type, score = 60. Otherwise score = 20.

#### 1.2 heroWinRateScore (weight 0.25)

Measures the hero's overall win rate.

```
heroWinRateScore = hero.getHeroWinRate()
```

Hero's heroWinRate field is already a percentage (e.g., 52.5 means 52.5%).
This score is used directly (capped at 0–100).

#### 1.3 teamNeedScore (weight 0.20)

Measures whether the team lacks this hero's role type.

| Condition | Score |
|-----------|-------|
| Team has NO hero of this type (role gap) | 100 |
| Team has 1 hero of this type | 50 |
| Team has 2+ heroes of this type | 10 |

**How it works**: Look at all players in the team. Count how many heroes
of each type the team members own. If the candidate hero's type is missing
from the team entirely, score = 100 (the team really needs this role).

#### 1.4 playerHistoryScore (weight 0.15)

Measures whether the player already owns similar heroes.

| Condition | Score |
|-----------|-------|
| Player does NOT own this hero yet | 100 |
| Player already owns this hero | 0 |

**How it works**: Simple check — if the player already has this hero in
their heroIdList, score = 0. Otherwise score = 100. This prevents
recommending heroes the player already owns.

#### 1.5 equipmentCompatibilityScore (weight 0.10)

Measures how well-equipped the candidate hero is.

```
equipmentCompatibilityScore = min(recommendedEquipCount × 20, 100)
```

Heroes with more recommended equipment items score higher (up to 100 at 5 items).

---

## 2. Equipment Recommendation Score

### Formula

```
Equipment Score =
  0.35 × heroTypeMatchScore
+ 0.30 × usageCountScore
+ 0.20 × winRateSupportScore
+ 0.15 × playerPreferenceScore
```

Each sub-score ranges from 0 to 100. Final score ranges from 0 to 100.
Equipment items are ranked by final score (descending).

### Sub-Score Definitions

#### 2.1 heroTypeMatchScore (weight 0.35)

Measures whether the equipment type is suitable for the hero's role.

**Compatibility Table:**

| Equipment Type | Suitable Hero Types |
|----------------|---------------------|
| ATTACK | ASSASSIN, MARKSMAN, WARRIOR |
| DEFENSE | TANK, WARRIOR |
| MOVEMENT | ALL types |
| MAGIC | MAGE |
| JUNGLE | ASSASSIN, WARRIOR |
| SUPPORT | SUPPORT, TANK |

If the equipment type matches → score = 100, otherwise score = 15.

#### 2.2 usageCountScore (weight 0.30)

Measures how popular the equipment is (how many heroes recommend it).

```
usageCountScore = min(usageCount × 10, 100)
```

Equipment with usageCount = 10 or more gets the full 100 points.

#### 2.3 winRateSupportScore (weight 0.20)

Measures whether high-win-rate heroes use this equipment.

```
winRateSupportScore = average win rate of heroes that recommend this equipment
```

For each hero that recommends this equipment, average their heroWinRate values.
If no heroes recommend it, score = 0.

#### 2.4 playerPreferenceScore (weight 0.15)

Measures whether the player's owned heroes recommend this equipment.

```
playerPreferenceScore = (count of player's heroes that recommend this equip / total player heroes) × 100
```

If the player owns 5 heroes and 3 of them recommend this equipment, score = 60.

---

## 3. Scoring Summary Table

| Factor | Hero Weight | Equipment Weight |
|--------|-------------|------------------|
| Type Preference / Match | 0.30 | 0.35 |
| Win Rate | 0.25 | 0.20 |
| Team Need | 0.20 | — |
| Player History | 0.15 | 0.15 |
| Equipment Compatibility / Usage | 0.10 | 0.30 |
| **Total** | **1.00** | **1.00** |

---

## 4. How to Read This Formula

- **Score range**: 0 to 100 for every sub-score and the final score.
- **Weights**: Each weight is a decimal that adds up to 1.0 (100%).
  Multiply each sub-score by its weight, then add them together.
- **Higher score = better recommendation**.
- **Simple example**: If a hero scores 80 on typePreference, 60 on winRate,
  50 on teamNeed, 100 on playerHistory, 40 on equipmentCompat:
  Score = 0.30×80 + 0.25×60 + 0.20×50 + 0.15×100 + 0.10×40
        = 24 + 15 + 10 + 15 + 4 = 68

---

## 5. Sub-Score Calculation Logic (Java Implementation)

### 5.1 Hero Sub-Scores

#### typePreferenceScore — Player's Hero Type Preference

```
1. Count player's owned heroes by HeroType (e.g. 3 Assassin, 1 Mage)
2. Find the type with the highest count → "favoriteType"
3. Compare candidate hero's type:
   - Same as favoriteType     → score = 100
   - Player owns any of same type → score = 60
   - No match                 → score = 20
```

Implemented in `RecommendationService.calcTypePreference()`.

#### heroWinRateScore — Hero's Own Win Rate

```
score = min(hero.getHeroWinRate(), 100)
```
The `heroWinRate` field is directly used as the score (capped at 100).
Heroes like Diao Chan (56.0%) score higher than Luban No.7 (47.2%).

Implemented in `RecommendationService.calcHeroWinRateScore()`.

#### teamNeedScore — Team Role Gap Detection

```
1. Count how many heroes of candidate's type exist in the entire team
   (iterate all team members → their heroIdLists → hero types)
2. Score by scarcity:
   - 0 heroes of this type in team → score = 100 (urgent need)
   - 1 hero of this type in team  → score = 50
   - 2+ heroes of this type       → score = 10 (already covered)
   - No team (team == null)       → score = 50 (neutral)
```

Implemented in `RecommendationService.calcTeamNeedScore()`.

#### playerHistoryScore — Already Owned Check

```
if (player.getHeroIdList().contains(hero.getId()))
    score = 0   // already owned, don't recommend again
else
    score = 100  // not yet owned, good candidate
```

Implemented in `RecommendationService.calcPlayerHistoryScore()`.

#### equipmentCompatibilityScore — How Many Equipment Items

```
score = min(hero.getRecommendedEquipIds().size() × 20, 100)
```
Hero with 5+ recommended equipment items gets max score (100).
Hero with 2 items (e.g. Wang Zhaojun) scores 40.

Implemented in `RecommendationService.calcEquipmentCompatibilityScore()`.

---

### 5.2 Equipment Sub-Scores

#### heroTypeMatchScore — Equipment-to-Hero Compatibility

Uses a hardcoded compatibility table in `isEquipmentCompatible()`:

| Equipment Type | Compatible Hero Types |
|----------------|----------------------|
| ATTACK | ASSASSIN, MARKSMAN, WARRIOR |
| DEFENSE | TANK, WARRIOR |
| MAGIC | MAGE |
| MOVEMENT | ALL types |
| JUNGLE | ASSASSIN, WARRIOR |
| SUPPORT | SUPPORT, TANK |

```
if (isEquipmentCompatible(hero.getHeroType(), equip.getEquipType()))
    score = 100
else
    score = 15  // still somewhat visible, not zero
```

Implemented in `RecommendationService.calcHeroTypeMatchScore()`.

#### usageCountScore — Equipment Popularity

```
score = min(equip.getUsageCount() × 10, 100)
```
Usage count comes from how many heroes recommend this equipment via `addEquipmentToHero()`.
Equipment with usage=10 or more → max score 100.

Implemented in `RecommendationService.calcUsageCountScore()`.

#### winRateSupportScore — Average Win Rate of Recommending Heroes

```
1. Find all heroes that recommend this equipment
2. Average their heroWinRate values
3. If no heroes recommend it → score = 0
```
Equipment recommended by high-win-rate heroes (e.g. Breaker used by 4 heroes averaging ~51%) scores higher.

Implemented in `RecommendationService.calcWinRateSupportScore()`.

#### playerPreferenceScore — Player's Heroes Using This Equipment

```
totalHeroes = player.getHeroIdList().size()
matchCount  = count of player's heroes that recommend this equipment

if (totalHeroes == 0) score = 50  // neutral for new players
else score = (matchCount / totalHeroes) × 100
```
If player owns 3 heroes and 2 of them recommend this equipment → score = 66.7.

Implemented in `RecommendationService.calcPlayerPreferenceScore()`.

---

## 6. Test Results (T16–T18)

### T16 — MengLei (AG, owns 3 Assassins)
| Rank | Hero | Type | WR | Key Reason |
|------|------|------|----|-----------|
| 1 | Lanling Wang | Assassin | 49.0% | matches preference |
| 2 | Arthur | Warrior | 54.3% | **team needs this role**, high WR |
| 3 | Lu Bu | Warrior | 51.5% | **team needs this role** |
| 4 | Li Bai | Assassin | 52.5% | already owned, matches preference |
| 5 | Sun Wukong | Assassin | 50.1% | already owned, matches preference |

AG team has no Warrior → Arthur/Lu Bu get teamNeed bonus and jump ahead of some owned Assassins.
Han Xin (48.3%) not in top 5 — low win rate penalizes it.

### T17 — TanRan (eStar, owns Tank+Warrior)
| Rank | Hero | Type | WR | Key Reason |
|------|------|------|----|-----------|
| 1 | Lu Bu | Warrior | 51.5% | balanced choice |
| 2 | Cheng Yaojin | Tank | 53.1% | already owned, matches preference, high WR |
| 3 | Hou Yi | Marksman | 50.9% | balanced choice |
| 4 | Luban No.7 | Marksman | 47.2% | balanced choice |
| 5 | Lian Po | Tank | 48.8% | already owned, matches preference |

No SUPPORT heroes recommended because the dataset contains ZERO Support-type heroes.
The teamNeed scoring logic is correct — it would give 100 to any Support hero — but no such hero exists in the 15-hero dataset.

### T18 — Equipment for Li Bai (Assassin)
| Rank | Equipment | Type | Price | Usage | Key Reason |
|------|-----------|------|-------|-------|-------------|
| 1 | Breaker | Attack | 2950 | 4 | recommended for hero, popular, used by player's heroes |
| 2 | Infinity Edge | Attack | 2140 | 4 | recommended for hero, popular, used by player's heroes |
| 3 | Grandmaster | Attack | 2100 | 2相容 | used by player's heroes |
| 4 | Calm Boots | Movement | 710 | 3 | recommended for hero, popular |
| 5 | Lightning Dagger | Attack | 1840 | 3 | popular |

All top items are ATTACK type (matching Assassin compatibility). Items directly recommended for Li Bai rank highest.
