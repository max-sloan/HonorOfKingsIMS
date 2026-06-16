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
