module.exports = {
  root: true,
  parser: '@typescript-eslint/parser',
  parserOptions: {
    project: './tsconfig.json',
  },
  extends: [
    'eslint:recommended',
    '@react-native-community',
    //'plugin:prettier/recommended',
  ],
  rules: {
    // 0 false, 1 warn, 2 error
    'prettier/prettier': 0
  },
  ignorePatterns: ['.eslintrc.js']
};

