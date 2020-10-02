package com.archyx.aureliumskills.skills;

import com.archyx.aureliumskills.skills.abilities.Ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerSkillInstance {

    private final UUID playerId;
    private final String playerName;

    private final Map<Skill, Integer> levels = new HashMap<>();
    private final Map<Skill, Double> xp = new HashMap<>();
    private final Map<Ability, Integer> abilities = new HashMap<>();

    public PlayerSkillInstance(PlayerSkill playerSkill) {
        this.playerId = playerSkill.getPlayerId();
        this.playerName = playerSkill.getPlayerName();
        for (Skill skill : Skill.values()) {
            levels.put(skill, playerSkill.getSkillLevel(skill));
            xp.put(skill, playerSkill.getXp(skill));
        }
        for (Ability ability : Ability.values()) {
            abilities.put(ability, playerSkill.getAbilityLevel(ability));
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public Map<Ability, Integer> getAbilities() {
        return abilities;
    }

    public int getAbilityLevel(Ability ability) {
        return abilities.get(ability);
    }

    public double getXp(Skill skill) {
        if (xp.containsKey(skill)) {
            return xp.get(skill);
        }
        else {
            return 0;
        }
    }

    public int getSkillLevel(Skill skill) {
        return levels.getOrDefault(skill, 0);
    }

    public Set<Skill> getSkillSet() {
        return levels.keySet();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getPowerLevel() {
        int power = 0;
        for (int level : levels.values()) {
            power += level;
        }
        return power;
    }

    public double getPowerXp() {
        double powerXp = 0.0;
        for (double skillXp : xp.values()) {
            powerXp += skillXp;
        }
        return powerXp;
    }
}